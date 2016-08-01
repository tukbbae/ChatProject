package com.hmlee.chatchat.model.domain;

import org.hibernate.annotations.Type;

import com.hmlee.chatchat.core.base.BaseObject;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by hmlee
 */
@Entity
public class Statistic extends BaseObject {

    private static final long serialVersionUID = -2155619544513136744L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idx;
    private String sender;
    private String senderPhoneNumber;
    private String receiver;
    private String receiverPhoneNumber;
    private Date sendDate;
    private Date receiveDate;
    @Column(nullable = false)
    private Long msgId;
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isRead;
    @Column(length = 1, nullable = false)
    private String type;

    public Statistic() {
    }

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
