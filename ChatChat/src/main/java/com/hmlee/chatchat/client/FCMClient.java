package com.hmlee.chatchat.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hmlee.chatchat.model.FCMBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by hmlee
 */
@Component
public class FCMClient {

    private final static String API_KEY = "AIzaSyD1TY9ZO5JBz3A7Ad8_nN7hRNenQo2c6Ss";
    private static FCMClient fcmClient = null;

    private static FCMBody fcmBody;

    private static Thread sThread;

    public static FCMClient getInstance() {
        if (fcmClient == null) {
        	fcmClient = new FCMClient();
        }

        return fcmClient;
    }

    public void sendFCM(List<String> registrationIds, String senderName, String senderMail, String msg, String type) {
        if (registrationIds == null || registrationIds.size() == 0) {
            throw new RuntimeException("Not exist registration id list");
        }

        fcmBody = new FCMBody();
        for (String regiId : registrationIds) {
        	fcmBody.addRegId(regiId);
        }

        fcmBody.createData(type, senderName, senderMail, msg);

        sThread = new Thread(null, sending, "SEND");
        sThread.start();
    }

    private static Runnable sending = new Runnable() {

        private Logger logger = LoggerFactory.getLogger(this.getClass());

        @Override
        public void run() {
            String endpoint = "https://fcm.googleapis.com/fcm/send";

            URL url = null;
            try {
                url = new URL(endpoint);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Authorization", "key=" + URLEncoder.encode(API_KEY, "utf-8"));
                ObjectMapper mapper = new ObjectMapper();
                OutputStream wr = new DataOutputStream(conn.getOutputStream());
                mapper.writeValue(wr, fcmBody);
                wr.flush();
                wr.close();

                int status = conn.getResponseCode();
                if (status != 200) {
                    logger.debug("FCM response => {}", conn.getResponseMessage());
                    throw new IOException("Post failed with error code " + status);
                }
                logger.info("FCM Send OK!!");
            } catch (Exception e) {
                logger.debug("Exception at send fcm message!!!");
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

        }
    };

}
