package com.hmlee.chatchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.hmlee.chatchat.client.FCMClient;
import com.hmlee.chatchat.core.base.BaseService;
import com.hmlee.chatchat.core.constant.Constants;
import com.hmlee.chatchat.core.util.ListUtils;
import com.hmlee.chatchat.model.AckBody;
import com.hmlee.chatchat.model.JsonResult;
import com.hmlee.chatchat.model.UnreadMessage;
import com.hmlee.chatchat.model.domain.Friend;
import com.hmlee.chatchat.model.domain.Message;
import com.hmlee.chatchat.model.domain.Statistic;
import com.hmlee.chatchat.model.domain.User;
import com.hmlee.chatchat.repository.FriendRepository;
import com.hmlee.chatchat.repository.MessageRepository;
import com.hmlee.chatchat.repository.StatisticRepository;
import com.hmlee.chatchat.repository.UserRepository;

import java.util.*;

import javax.mail.internet.MimeMessage;

/**
 * ChatRestService
 *
 * REST API의 비즈니스 로직을 처리하는 Service
 *
 * Created by hmlee
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ChatRestService extends BaseService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private StatisticRepository statisticRepository;
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${server.address}")
    private String serverHost;

    @Value("${server.port}")
    private String serverPort;

	public boolean isRegisteredAddress(String email) {
		User user = userRepository.findUserByEmail(email);
		logger.debug("Find result => {}", user);
		return (user != null);
	}
    
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public boolean registerUser(String email, String name, String token, String deviceType) {

        User foundUser = userRepository.findUserByEmail(email);
        if (foundUser == null) {
        	User newUser = new User(email, name, token, deviceType);
        	userRepository.save(newUser);
            return true;
        } else {
        	return false;
        }
    }
    
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public boolean updateUser(String email, String name, String token, String deviceType) {

        User foundUser = userRepository.findUserByEmail(email);
        if (foundUser != null) {
        	foundUser.setName(name);
        	foundUser.setToken(token);
        	foundUser.setDevice_type(deviceType);
        	userRepository.save(foundUser);
            return true;
        } else {
        	return false;
        }
    }
    
	public List<User> getFriendList(String userEmail) {
		Iterable<User> iterable = null;
		iterable = friendRepository.findFriendListByUserId(userEmail);
		return ListUtils.toList(iterable);
	}

	@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
	public boolean registerFriend(String userEmail, String friendEmail) {
		User foundFriend = userRepository.findUserByEmail(friendEmail);
		if (foundFriend != null) {
			Friend friend = new Friend();
			friend.setUserEmail(userEmail);
			friend.setFriend(foundFriend);
			friendRepository.save(friend);
			return true;
		} else {
			return false;
		}
	}
	
	@Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
	public JsonResult sendFCMMessage(String senderEmail, String receiverEmail, String message, Locale locale) {
		JsonResult result = new JsonResult();

		User foundReceiveUser = userRepository.findUserByEmail(receiverEmail);
		String receiverToken = null;

		if (foundReceiveUser != null) {
			receiverToken = foundReceiveUser.getToken();
		} else {
			result.setResultCode(Constants.ResponseCode.NOT_FOUND);
			result.setResultMessage(
					messageSource.getMessage("app.api.sendMessage.failed.reason.notExistReceiver", null, locale));
			return result;
		}

		// save message to db
		Message messageObject = new Message(message);
		messageObject = messageRepository.saveAndFlush(messageObject);

		User foundSendUser = userRepository.findUserByEmail(senderEmail);
		String senderName = null;
		if (foundSendUser != null) {
			senderName = foundSendUser.getName();
		} else {
			result.setResultCode(Constants.ResponseCode.NOT_FOUND);
			result.setResultMessage(
					messageSource.getMessage("app.api.sendMessage.failed.reason.notExistSender", null, locale));
			return result;
		}
		
		String receiverDeviceType = foundReceiveUser.getDevice_type();
		
		if (receiverDeviceType.equalsIgnoreCase("A")) {
			try {
				// FCM 발송
				FCMClient.getInstance().sendFCM(receiverToken, senderName, senderEmail, message);
			} catch (Exception e) {
				logger.error("## IO.EXT.FCM Exception");
				e.printStackTrace();
				result.setResultCode(Constants.ResponseCode.SERVER_ERROR);
				result.setResultMessage(
						messageSource.getMessage("app.api.response.description.internalServerError", null, locale));
				return result;
			}
		} else if (receiverDeviceType.equalsIgnoreCase("W")) {
			String[] receivers = new String[1];
			receivers[0] = senderEmail;
			sendGeneralMessageMail(foundSendUser, receivers, message);
		} else {
			// TODO :: other device processing
		}
		
		result.setResultCode(Constants.ResponseCode.SUCCESS);
		result.setResultMessage(messageSource.getMessage("app.api.response.description.success", null, locale));

		return result;
	}

//    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
//    public JsonResult sendPushMessage(PushRequestBody requestBody, Locale locale) {
//
//        JsonResult result = new JsonResult();
//        logger.debug("Puhs requestBody => {}", requestBody);
//
//        // 발송할 메시지 목록 중 첫번째 메시지 정보에서 발신자 이메일을 가져온다.
//        // FIXME : 추후 수정이 필요한 항목
//        MessageBody firstReceiverMessage = requestBody.getPushList().get(0);
//        String senderEmail = firstReceiverMessage.getEmail();
//        Address senderAddress = addressRepository.findAddressByEmail(senderEmail);
//        if (senderAddress == null) {
//            result.setResultCode(Constants.ResponseCode.NOT_FOUND);
//            result.setResultMessage(messageSource.getMessage("app.api.sendMessage.failed.reason.notExistSender", null, locale));
//            return result;
//        }
//
//        String type = requestBody.getType();
//
//        if (StringUtils.isBlank(type)) {
//            result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
//            result.setResultMessage(messageSource.getMessage("app.api.response.description.badRequest", null, locale));
//            return result;
//        }
//
//        String sendMessage = null;
//        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date sendDate = new Date();
//        if (Constants.MessageClass.NORMAL.equals(type)) {
//            sendMessage = firstReceiverMessage.getMessage();
//        } else {
//            // TODO :: 추후 다른 타입의 메시지 올 경우 처리 필요
//        }
//
//        // save message to db
//        Message message = new Message(sendMessage);
//        message = messageRepository.saveAndFlush(message);
//
//        // 수신자의 단말 유형에 따라 발신 목록을 구성
//        List<String> fcmReceiverList = new ArrayList<>();
//        List<String> mailReceiverList = new ArrayList<>();
//        for (MessageBody messageBody : requestBody.getPushList()) {
//            Address receiver = addressRepository.findAddressByEmail(messageBody.getEmail());
//
//            if (receiver != null) {
//
//                Statistic statistic = new Statistic();
//                statistic.setSender(senderAddress.getName());
//                statistic.setSenderPhoneNumber(senderAddress.getPhoneNumber());
//                statistic.setReceiver(receiver.getName());
//                statistic.setReceiverPhoneNumber(receiver.getPhoneNumber());
//                statistic.setSendDate(sendDate);
//                statistic.setMsgId(message.getIdx());
//
//                if (receiver.getRegiId() != null) {
//                    if (Constants.DeviceType.IOS.equalsIgnoreCase(receiver.getDeviceType())) {
//	
//                    } else {
//                        fcmReceiverList.add(receiver.getRegiId());
//                        statistic.setReceiveDate(new Date());
//                        statistic.setIsRead(true);
//                    }
//                    statistic.setType(Constants.MessageType.PUSH_MESSAGE);
//                } else {
//                    mailReceiverList.add(receiver.getEmail());
//                    statistic.setIsRead(true);
//                    statistic.setType(Constants.MessageType.MAIL);
//                }
//
//                statisticRepository.save(statistic);
//            } else {
//                result.setResultCode(Constants.ResponseCode.NOT_FOUND);
//                result.setResultMessage(messageSource.getMessage("app.api.sendMessage.failed.reason.notExistReceiver", null, locale));
//            }
//        }
//        statisticRepository.flush();
//
//        if (fcmReceiverList.size() > 0) {
//            try {
//                // FCM 발송
//                FCMClient.getInstance().sendFCM(fcmReceiverList, senderAddress.getName(), senderEmail, sendMessage, type);
//            } catch (Exception e) {
//                logger.error("## IO.EXT.FCM Exception");
//                e.printStackTrace();
//                result.setResultCode(Constants.ResponseCode.SERVER_ERROR);
//                result.setResultMessage(messageSource.getMessage("app.api.response.description.internalServerError", null, locale));
//                return result;
//            }
//        }
//
//        if (mailReceiverList.size() > 0) {
//            // 메일 발송
//            String[] receivers = mailReceiverList.toArray(new String[mailReceiverList.size()]);
//            sendGeneralMessageMail(senderAddress, receivers, sendMessage);
//        }
//
//        result.setResultCode(Constants.ResponseCode.SUCCESS);
//        if (Constants.MessageClass.NORMAL.equals(type)) {
//            result.setResultMessage(messageSource.getMessage("app.api.response.description.success", null, locale));
//        } else {
//            String url = "http://"+serverHost+":"+serverPort+"/attendance/"+dateFormat.format(sendDate);
//            result.setResultMessage(url);
//        }
//
//        return result;
//    }
    
    private boolean sendGeneralMessageMail(User sender, String[] receivers, String content) {
        Locale locale = LocaleContextHolder.getLocale();
        final Context ctx = new Context(locale);
        ctx.setVariable("sendDate", new Date());
        ctx.setVariable("message", content);

        try {
            // Prepare message using a Spring helper
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
            String[] args = {sender.getName()};
            message.setSubject(messageSource.getMessage("ui.email.general.subject", args, locale));
            message.setTo(receivers);

            // Create the HTML body using Thymeleaf
            String htmlContent = templateEngine.process("mails/general_msg_mail", ctx);
            message.setText(htmlContent, true); // true = isHtml

            // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
            // final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
            // message.addInline(imageResourceName, imageSource, imageContentType);

            // Send mail
            mailSender.send(mimeMessage);

            return true;
        } catch (Exception e) {
            logger.error("## SEND GENERAL MAIL ERROR!!!");
            e.printStackTrace();
            return false;
        }
    }

    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void updateMessageReadStatus(List<AckBody> ackList) {
        for (AckBody ack : ackList) {
            User user = userRepository.findUserByToken(ack.getRegi_id());
            Statistic statistic = statisticRepository.findMessageWithReadStatus(Long.parseLong(ack.getMsg_id()), user.getEmail(), false);
            if (statistic != null) {
                statistic.setIsRead(true);
                statisticRepository.save(statistic);
            }
        }
        statisticRepository.flush();
    }

    public List<UnreadMessage> findUnreadMessageList(String regiId) {
    	User user = userRepository.findUserByToken(regiId);
        List<Statistic> statisticList = statisticRepository.findUnreadMessage(user.getEmail());
        List<UnreadMessage> unreadList = new ArrayList<UnreadMessage>();
        for (Statistic statistic : statisticList) {
            Message message = messageRepository.findOne(statistic.getMsgId());
            UnreadMessage unreadMessage = new UnreadMessage();
            unreadMessage.setMessageId(message.getIdx());
            unreadMessage.setMessage(message.getContent());
            unreadMessage.setSenderName(statistic.getSender());
            unreadMessage.setSenderNumber(statistic.getSenderPhoneNumber());
            unreadMessage.setSentDate(statistic.getSendDate());
            unreadList.add(unreadMessage);
        }

        return unreadList;
    }

}
