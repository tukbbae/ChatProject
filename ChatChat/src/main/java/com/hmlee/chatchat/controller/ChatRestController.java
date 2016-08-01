package com.hmlee.chatchat.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import com.hmlee.chatchat.core.base.BaseController;
import com.hmlee.chatchat.core.constant.Constants;
import com.hmlee.chatchat.model.AckRequestBody;
import com.hmlee.chatchat.model.JsonResult;
import com.hmlee.chatchat.model.PushRequestBody;
import com.hmlee.chatchat.model.UnreadMessage;
import com.hmlee.chatchat.model.domain.Address;
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

    @RequestMapping(value = "/regi_request", method = RequestMethod.POST)
    public JsonResult registerRegiId(@RequestBody Map<String, String> requestBody, Locale locale) throws Exception {
        logger.info("[====== Start of registerRegiId method ======]");

        String regiId = requestBody.get("regi_id");
        String phoneNumber = requestBody.get("phone_number");
        String deviceType = requestBody.get("device_type");

        JsonResult result = new JsonResult();

        if (regiId == null) {
            result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
            result.setResultMessage(messageSource.getMessage("app.api.registerRegiId.failed.reason.regiIdIsRequeired", null, locale));
            return result;
        }
        if (phoneNumber == null) {
            result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
            result.setResultMessage(messageSource.getMessage("app.api.registerRegiId.failed.reason.phoneNumberIsRequired", null, locale));
            return result;
        }
        if (deviceType == null) {
            result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
            result.setResultMessage(messageSource.getMessage("app.api.registerRegiId.failed.reason.deviceTypeIsRequired", null, locale));
            return result;
        }

        if (restService.registerDeviceIdentifier(regiId, phoneNumber, deviceType)) {
            result.setResultCode(Constants.ResponseCode.SUCCESS);
            result.setResultMessage(messageSource.getMessage("app.api.response.description.success", null, locale));
        } else  {
            result.setResultCode(Constants.ResponseCode.FAILED);
            result.setResultMessage(messageSource.getMessage("app.api.response.description.failed", null, locale));
        }

        logger.info("[====== End of registerRegiId method ======]");
        return result;
    }

    @RequestMapping(value = "/isRegistered", method = RequestMethod.POST)
    public boolean isRegistered(@RequestBody Map<String, String> requestBody, Locale locale) throws Exception {
        logger.info("[====== Start of registerRegiId method ======]");
        logger.debug("body => {}", requestBody);
        boolean result = restService.isRegisteredAddress(requestBody.get("phone_number"));

        logger.info("[====== End of registerRegiId method ======]");
        return result;
    }

    @RequestMapping(value = "/push_request")
    public JsonResult sendPushMessage(@RequestBody PushRequestBody requestBody, Locale locale) throws Exception {
        logger.info("[====== Start of sendPushMessage method ======]");

        if(requestBody == null) {
            JsonResult result = new JsonResult();
            result.setResultCode(Constants.ResponseCode.BAD_REQUEST);
            result.setResultMessage(messageSource.getMessage("app.api.response.description.badRequest", null, locale));
            return result;
        }

        JsonResult result = restService.sendPushMessage(requestBody, locale);

        logger.info("[====== End of sendPushMessage method ======]");

        return result;
    }

    @RequestMapping(value = "/address_request")
    public List<Address> getAddressList(@RequestParam(value = "did", required = false, defaultValue = "") String did, Locale locale) throws  Exception {
        return restService.getAddressList(did);
    }

    @RequestMapping(value = "/auth_device")
    public JsonResult authDevice(@RequestParam(value = "authKey", required = true) String authKey, Locale locale) throws Exception {
        return restService.completeAuthentication(authKey, locale);
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
