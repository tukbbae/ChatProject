package com.hmlee.chatchat.service;

import org.apache.commons.lang3.StringUtils;
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

import com.hmlee.chatchat.core.base.BaseService;
import com.hmlee.chatchat.core.constant.Constants;
import com.hmlee.chatchat.core.crypto.AESCrypt;
import com.hmlee.chatchat.core.util.ListUtils;
import com.hmlee.chatchat.model.AckBody;
import com.hmlee.chatchat.model.JsonResult;
import com.hmlee.chatchat.model.MessageBody;
import com.hmlee.chatchat.model.PushRequestBody;
import com.hmlee.chatchat.model.UnreadMessage;
import com.hmlee.chatchat.model.domain.Address;
import com.hmlee.chatchat.model.domain.Message;
import com.hmlee.chatchat.model.domain.Statistic;
import com.hmlee.chatchat.repository.AddressRepository;
import com.hmlee.chatchat.repository.MessageRepository;
import com.hmlee.chatchat.repository.StatisticRepository;

import javax.mail.internet.MimeMessage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private AddressRepository addressRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${server.address}")
    private String serverHost;

    @Value("${server.port}")
    private String serverPort;

    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public boolean registerDeviceIdentifier(String regiId, String phoneNumber, String deviceType) {

        Address foundAddress = addressRepository.findAddressByPhoneNumber(phoneNumber);
        if (foundAddress != null) {
            if (!regiId.equals(foundAddress.getRegiId())) {
                if (Constants.DeviceType.IOS.equalsIgnoreCase(deviceType)) {
                    try {
                        List<String> keys = new ArrayList<String>();
                        keys.add(phoneNumber);
                        keys.add(regiId);

                        String authKey = AESCrypt.encrypt(StringUtils.join(keys, "|"));
//                        if (StringUtils.isNotBlank(foundAddress.getEmail())) {
//                            sendAuthenticateMail(authKey, foundAddress.getEmail());
//                            return true;
//                        } else {
//                            return false;
//                        }
                        return false;
                    } catch (Exception e) {
                        logger.error("## FAILED GENERATE DEVICE AUTH KEY!!!");
                        e.printStackTrace();
                        return false;
                    }
                } else {
                    foundAddress.setRegiId(regiId);
                    addressRepository.save(foundAddress);
                    return true;
                }
            } else {
                if (Constants.DeviceType.IOS.equalsIgnoreCase(deviceType)) {
                    try {
                        List<String> keys = new ArrayList<String>();
                        keys.add(phoneNumber);
                        keys.add(regiId);

                        String authKey = AESCrypt.encrypt(StringUtils.join(keys, "|"));
//                        if (StringUtils.isNotBlank(foundAddress.getEmail())) {
//                            sendAuthenticateMail(authKey, foundAddress.getEmail());
//                            return true;
//                        } else {
//                            return false;
//                        }
                        return false;
                    } catch (Exception e) {
                        logger.error("## FAILED GENERATE DEVICE AUTH KEY!!!");
                        e.printStackTrace();
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public boolean isRegisteredAddress(String phoneNumber) {
        Address address = addressRepository.findAddressByPhoneNumber(phoneNumber);
        logger.debug("Find result => {}", address);
        return (address != null);
    }

    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public JsonResult sendPushMessage(PushRequestBody requestBody, Locale locale) {

        JsonResult result = new JsonResult();
        logger.debug("Puhs requestBody => {}", requestBody);

        // 발송할 메시지 목록 중 첫번째 메시지 정보에서 발신자 번호를 가져온다.
        // FIXME : 추후 수정이 필요한 항목
        MessageBody firstReceiverMessage = requestBody.getPushList().get(0);
        String senderPhoneNumber = firstReceiverMessage.getSender();
        Address senderAddress = addressRepository.findAddressByPhoneNumber(senderPhoneNumber);
        if (senderAddress == null) {
            result.setResultCode(Constants.ResponseCode.NOT_FOUND);
            result.setResultMessage(messageSource.getMessage("app.api.sendMessage.failed.reason.notExistSender", null, locale));
            return result;
        }

        String type = requestBody.getType();

        if (StringUtils.isBlank(type)) {
            result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
            result.setResultMessage(messageSource.getMessage("app.api.response.description.badRequest", null, locale));
            return result;
        }

        String sendMessage = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date sendDate = new Date();
        if (Constants.MessageClass.NORMAL.equals(type)) {
            sendMessage = firstReceiverMessage.getMessage();
        } else {
            sendMessage = "http://"+serverHost+":"+serverPort+"/api/attendance/"+dateFormat.format(sendDate);
        }

        // save message to db
        Message message = new Message(sendMessage);
        message = messageRepository.saveAndFlush(message);

        // 수신자의 단말 유형에 따라 발신 목록을 구성
        List<String> apnsReceiverList = new ArrayList<>();
        List<String> gcmReceiverList = new ArrayList<>();
        List<String> mailReceiverList = new ArrayList<>();
        for (MessageBody messageBody : requestBody.getPushList()) {
            Address receiver = addressRepository.findAddressByPhoneNumber(messageBody.getPhone_number());

            if (receiver != null) {

                Statistic statistic = new Statistic();
                statistic.setSender(senderAddress.getName());
                statistic.setSenderPhoneNumber(senderAddress.getPhoneNumber());
                statistic.setReceiver(receiver.getName());
                statistic.setReceiverPhoneNumber(receiver.getPhoneNumber());
                statistic.setSendDate(sendDate);
                statistic.setMsgId(message.getIdx());

                if (receiver.getRegiId() != null) {
                    if (Constants.DeviceType.IOS.equalsIgnoreCase(receiver.getDeviceType())) {
                        apnsReceiverList.add(receiver.getRegiId());
                        statistic.setIsRead(false);
                    } else {
                        gcmReceiverList.add(receiver.getRegiId());
                        statistic.setReceiveDate(new Date());
                        statistic.setIsRead(true);
                    }
                    statistic.setType(Constants.MessageType.PUSH_MESSAGE);
                } else {
                    mailReceiverList.add(receiver.getEmail());
                    statistic.setIsRead(true);
                    statistic.setType(Constants.MessageType.MAIL);
                }

                statisticRepository.save(statistic);
            } else {
                result.setResultCode(Constants.ResponseCode.NOT_FOUND);
                result.setResultMessage(messageSource.getMessage("app.api.sendMessage.failed.reason.notExistReceiver", null, locale));
            }
        }
        statisticRepository.flush();

        if (gcmReceiverList.size() > 0) {
        	// TODO :: FCM 발송 기능
//            try {
//                // GCM 발송
//                GCMClient.getInstance().sendGCM(gcmReceiverList, senderAddress.getName(), senderPhoneNumber, sendMessage, type);
//            } catch (Exception e) {
//                logger.error("## IO.EXT.GCM Exception");
//                e.printStackTrace();
//                result.setResultCode(Constants.ResponseCode.SERVER_ERROR);
//                result.setResultMessage(messageSource.getMessage("app.api.response.description.internalServerError", null, locale));
//                return result;
//            }
        }

//        if (apnsReceiverList.size() > 0) {
//            // APNS 발송
//            for (String deviceToken : apnsReceiverList) {
//                Address receiver = addressRepository.findAddressByregiId(deviceToken);
//                int unreadCount = statisticRepository.unreadCountByReceiverPhoneNumber(receiver.getPhoneNumber());
//                // FIXME : APNS는 길이 제한이 있으므로 메시지를 먼저 DB에 저장하고 단말에서 조회 요청을 해서 메시지 전문을 확인하는 방식으로 수정 필요
//                APNSClient.getInstance().sendAPNS(type, deviceToken, senderAddress.getName(), senderPhoneNumber, sendMessage, Constants.ApnsMessageType.GENERAL_MESSAGE, message.getIdx().intValue(), unreadCount);
//            }
//        }

//        if (mailReceiverList.size() > 0) {
//            // 메일 발송
//            String[] receivers = mailReceiverList.toArray(new String[mailReceiverList.size()]);
//            sendGeneralMessageMail(senderAddress, receivers, sendMessage);
//        }

        result.setResultCode(Constants.ResponseCode.SUCCESS);
        if (Constants.MessageClass.NORMAL.equals(type)) {
            result.setResultMessage(messageSource.getMessage("app.api.response.description.success", null, locale));
        } else {
            String url = "http://"+serverHost+":"+serverPort+"/attendance/"+dateFormat.format(sendDate);
            result.setResultMessage(url);
        }

        return result;
    }

    public List<Address> getAddressList(String did) {
        Iterable<Address> iterable = null;
        if (StringUtils.isBlank(did)) {
            iterable = addressRepository.findAll();
        } else {
//            Department department = departmentRepository.findOne(Long.parseLong(did));
//            if (department == null) {
//                return null;
//            }
        }

        return ListUtils.toList(iterable);
    }

    public JsonResult completeAuthentication(String authKey, Locale locale) {
        JsonResult result = new JsonResult();

        try {
            String decrytedKey = AESCrypt.decrypt(authKey);
            String[] keys = decrytedKey.trim().split("\\|");

            if (keys.length != 2) {
                result.setResultCode(Constants.ResponseCode.FAILED);
                result.setResultMessage(messageSource.getMessage("app.authentication.result.invalidAuthKey", null, locale));
            } else {
                String phoneNumber = keys[0];
                String deviceToken = keys[1];

                Address address = addressRepository.findAddressByPhoneNumber(phoneNumber);
                logger.debug("Find result => {}", address);
                if (address != null) {
                    address.setRegiId(deviceToken);
                    address.setDeviceType(Constants.DeviceType.IOS);
                    addressRepository.save(address);

//                    APNSClient.getInstance().sendAPNS(Constants.MessageClass.NORMAL, deviceToken,
//                            null, null,
//                            messageSource.getMessage("app.authentication.result.success", null, locale),
//                            Constants.ApnsMessageType.AUTHENTICATION, 0, 0);

                    result.setResultCode(Constants.ResponseCode.SUCCESS);
                    result.setResultMessage(messageSource.getMessage("app.authentication.result.success", null, locale));
                } else {
                    result.setResultCode(Constants.ResponseCode.FAILED);
                    result.setResultMessage(messageSource.getMessage("app.authentication.result.notExistAddress", null, locale));
                }
            }
        } catch (Exception e) {
            logger.error("## AUTHENTICATION ERROR ##");
            e.printStackTrace();
            result.setResultCode(Constants.ResponseCode.SERVER_ERROR);
            result.setResultMessage(messageSource.getMessage("app.api.response.description.internalServerError", null, locale));
            return result;
        }

        return result;
    }

    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void updateMessageReadStatus(List<AckBody> ackList) {
        for (AckBody ack : ackList) {
            Address address = addressRepository.findAddressByregiId(ack.getRegi_id());
            Statistic statistic = statisticRepository.findMessageWithReadStatus(Long.parseLong(ack.getMsg_id()), address.getPhoneNumber(), false);
            if (statistic != null) {
                statistic.setIsRead(true);
                statisticRepository.save(statistic);
            }
        }
        statisticRepository.flush();
    }

    public List<UnreadMessage> findUnreadMessageList(String regiId) {
        Address address = addressRepository.findAddressByregiId(regiId);
        List<Statistic> statisticList = statisticRepository.findUnreadMessage(address.getPhoneNumber());
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
