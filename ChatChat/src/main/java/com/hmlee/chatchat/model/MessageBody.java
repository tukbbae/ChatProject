package com.hmlee.chatchat.model;

import com.hmlee.chatchat.core.base.BaseObject;

/**
 * Created by hmlee
 */
public class MessageBody extends BaseObject {

    private static final long serialVersionUID = 1974045715277978334L;
    private String sender;
    private String receiver;
    private String message;
    private String phone_number;
    private String regi_id;
    private String email;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getRegi_id() {
        return regi_id;
    }

    public void setRegi_id(String regi_id) {
        this.regi_id = regi_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
