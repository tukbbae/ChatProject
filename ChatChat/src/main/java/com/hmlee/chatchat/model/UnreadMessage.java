package com.hmlee.chatchat.model;


import java.util.Date;

import com.hmlee.chatchat.core.base.BaseObject;

/**
 * Created by hmlee
 */
public class UnreadMessage extends BaseObject {

    private static final long serialVersionUID = -1425137687823178397L;

    private String message;
    private Long messageId;
    private String senderName;
    private String senderNumber;
    private Date sentDate;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
}
