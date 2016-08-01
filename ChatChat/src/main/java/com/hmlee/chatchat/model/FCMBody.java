package com.hmlee.chatchat.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by hmlee
 */
public class FCMBody implements Serializable {

    public List<String> registration_ids;
    public Map<String,String> data;

    public void addRegId(String regId){
        if(registration_ids == null)
            registration_ids = new LinkedList<>();
        registration_ids.add(regId);
    }

    public void createData(String type, String senderName, String senderMail, String message){
        if(data == null)
            data = new HashMap<>();

        data.put("type", type);
        data.put("senderName", senderName);
        data.put("senderMail", senderMail);
        data.put("message", message);
    }


}
