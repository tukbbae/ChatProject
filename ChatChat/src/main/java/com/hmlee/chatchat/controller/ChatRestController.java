package com.hmlee.chatchat.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import com.hmlee.chatchat.core.base.BaseController;
import com.hmlee.chatchat.core.constant.Constants;
import com.hmlee.chatchat.model.AckRequestBody;
import com.hmlee.chatchat.model.FriendResult;
import com.hmlee.chatchat.model.JsonResult;
import com.hmlee.chatchat.model.MessageBody;
import com.hmlee.chatchat.model.UnreadMessage;
import com.hmlee.chatchat.model.domain.User;
import com.hmlee.chatchat.service.ChatRestService;

import java.util.*;

/**
 * ChatRestController
 *
 * REST API의 요청을 접수하고 응답을 처리하는 Controller
 *
 * Created by hmlee
 */
@RestController
@RequestMapping("/api")
public class ChatRestController extends BaseController {

    @Autowired
    private ChatRestService restService;

    @Autowired
    private MessageSource messageSource;
    
	@RequestMapping(value = "/isRegistered", method = RequestMethod.POST)
	public JsonResult isRegistered(@RequestBody Map<String, String> requestBody, Locale locale) throws Exception {
		logger.info("[====== Start of isRegistered method ======]");
		logger.debug("body => {}", requestBody);
		
		JsonResult result = new JsonResult();
		
		if (restService.isRegisteredAddress(requestBody.get("email"))) {
			result.setResultCode(Constants.ResponseCode.SUCCESS);
			result.setResultMessage(messageSource.getMessage("app.api.isRegisterUser.true", null, locale));
		} else {
			result.setResultCode(Constants.ResponseCode.FAILED);
			result.setResultMessage(messageSource.getMessage("app.api.isRegisterUser.false", null, locale));
		}
		
		logger.info("[====== End of isRegistered method ======]");
		return result;
	}

	@RequestMapping(value = "/registerRequest", method = RequestMethod.POST)
	public JsonResult registerRequest(@RequestBody Map<String, String> requestBody, Locale locale) throws Exception {
		logger.info("[====== Start of registerRequest method ======]");

		String email = requestBody.get("email");
		String name = requestBody.get("name");
		String token = requestBody.get("token");
		String deviceType = requestBody.get("device_type");

		JsonResult result = new JsonResult();

		if (email == null) {
			result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
			result.setResultMessage(messageSource
					.getMessage("app.api.registerRegiId.failed.reason.emailIsRequired", null, locale));
			return result;
		}
		
		if (name == null) {
			result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
			result.setResultMessage(messageSource
					.getMessage("app.api.registerRegiId.failed.reason.nameIsRequired", null, locale));
			return result;
		}
		
		if (token == null) {
			result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
			result.setResultMessage(messageSource.getMessage("app.api.registerRegiId.failed.reason.tokenIsRequeired", null, locale));
			return result;
		}

		if (deviceType == null) {
			result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
			result.setResultMessage(messageSource
					.getMessage("app.api.registerRegiId.failed.reason.deviceTypeIsRequired", null, locale));
			return result;
		}

		if (restService.registerUser(email, name, token, deviceType)) {
			result.setResultCode(Constants.ResponseCode.SUCCESS);
			result.setResultMessage(messageSource.getMessage("app.api.response.description.success", null, locale));
		} else {
			result.setResultCode(Constants.ResponseCode.FAILED);
			result.setResultMessage(messageSource.getMessage("app.api.response.description.failed", null, locale));
		}

		logger.info("[====== End of registerRequest method ======]");
		return result;
	}
	
	@RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
	public JsonResult updateUserInfo(@RequestBody Map<String, String> requestBody, Locale locale) throws Exception {
		logger.info("[====== Start of updateUserInfo method ======]");

		String email = requestBody.get("email");
		String name = requestBody.get("name");
		String token = requestBody.get("token");
		String deviceType = requestBody.get("device_type");
		
		JsonResult result = new JsonResult();
		
		if (email == null) {
			result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
			result.setResultMessage(messageSource
					.getMessage("app.api.registerRegiId.failed.reason.emailIsRequired", null, locale));
			return result;
		}
		
		if (name == null) {
			result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
			result.setResultMessage(messageSource
					.getMessage("app.api.registerRegiId.failed.reason.nameIsRequired", null, locale));
			return result;
		}
		
		if (token == null) {
			result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
			result.setResultMessage(messageSource.getMessage("app.api.registerRegiId.failed.reason.tokenIsRequeired", null, locale));
			return result;
		}

		if (deviceType == null) {
			result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
			result.setResultMessage(messageSource
					.getMessage("app.api.registerRegiId.failed.reason.deviceTypeIsRequired", null, locale));
			return result;
		}

		if (restService.updateUser(email, name, token, deviceType)) {
			result.setResultCode(Constants.ResponseCode.SUCCESS);
			result.setResultMessage(messageSource.getMessage("app.api.response.description.success", null, locale));
		} else {
			result.setResultCode(Constants.ResponseCode.FAILED);
			result.setResultMessage(messageSource.getMessage("app.api.response.description.failed", null, locale));
		}
		
		logger.info("[====== End of updateUserInfo method ======]");
		return result;
	}
	
    @RequestMapping(value = "/getFriendList", method = RequestMethod.POST)
    public FriendResult getFriendList(@RequestBody Map<String, String> requestBody, Locale locale) throws  Exception {
    	logger.debug("body => {}", requestBody);
    	
    	FriendResult result = new FriendResult();
    	ArrayList<User> friendList = (ArrayList<User>) restService.getFriendList(requestBody.get("userEmail"));
    	
    	result.setFriendList(friendList);
    	
    	return result;
    }
	
    @RequestMapping(value = "/addFriendRequest", method = RequestMethod.POST)
    public JsonResult addFriendRequest(@RequestBody Map<String, String> requestBody, Locale locale) throws Exception {
    	logger.info("[====== Start of addFriendRequest method ======]");

		String userEmail = requestBody.get("userEmail");
		String friendEmail = requestBody.get("friendEmail");

		JsonResult result = new JsonResult();
    	
    	if (restService.registerFriend(userEmail, friendEmail)) {
			result.setResultCode(Constants.ResponseCode.SUCCESS);
			result.setResultMessage(messageSource.getMessage("app.api.response.description.success", null, locale));
		} else {
			result.setResultCode(Constants.ResponseCode.FAILED);
			result.setResultMessage(messageSource.getMessage("app.api.response.description.failed", null, locale));
		}

		logger.info("[====== End of addFriendRequest method ======]");
		return result;
    }
	
    @RequestMapping(value = "/messageRequest")
    public JsonResult messageRequest(@RequestBody MessageBody requestBody, Locale locale) throws Exception {
        logger.info("[====== Start of messageRequest method ======]");
        
		logger.debug("Message requestBody => {}", requestBody);
        
        if(requestBody == null) {
            JsonResult result = new JsonResult();
            result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
            result.setResultMessage(messageSource.getMessage("app.api.response.description.badRequest", null, locale));
            return result;
        }
        
        String senderEmail = requestBody.getSenderEmail();
		String receiverEmail = requestBody.getReceiverEmail();
		String message = requestBody.getMessage();
        
        JsonResult result = restService.sendFCMMessage(senderEmail, receiverEmail, message, locale);
        
        logger.info("[====== End of messageRequest method ======]");
        
        return result;
    }

    @RequestMapping(value = "/ack_message")
    public JsonResult ackMessage(@RequestBody AckRequestBody requestBody, Locale locale) throws Exception {
        logger.debug("requestBody => {}", requestBody);
        JsonResult result = new JsonResult();

        if (requestBody == null || requestBody.getAckList().size() == 0) {
            result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
            result.setResultMessage(messageSource.getMessage("app.api.response.description.badRequest", null, locale));
        } else {
            restService.updateMessageReadStatus(requestBody.getAckList());
            result.setResultCode(Constants.ResponseCode.SUCCESS);
            result.setResultMessage(messageSource.getMessage("app.api.response.description.success", null, locale));
        }

        return result;
    }

    @RequestMapping(value = "/get_unread_msg")
    public Map<String, List<UnreadMessage>> getUnreadMessageList(@RequestBody Map<String, String> requestBody) throws Exception {
        String regiId = requestBody.get("regi_id");
        if (StringUtils.isBlank(regiId)) {
            return null;
        } else {
            List<UnreadMessage> unreadList = restService.findUnreadMessageList(regiId);
            Map<String, List<UnreadMessage>> resultMap = new HashMap<>();
            resultMap.put("unreadList", unreadList);
            return resultMap;
        }
    }

}
