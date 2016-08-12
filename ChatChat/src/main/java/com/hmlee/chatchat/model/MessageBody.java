package com.hmlee.chatchat.model;

import com.hmlee.chatchat.core.base.BaseObject;

/**
 * Created by hmlee
 */
public class MessageBody extends BaseObject {

	private static final long serialVersionUID = 1974045715277978334L;
	private String senderEmail;
	private String receiverEmail;
	private String message;

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
