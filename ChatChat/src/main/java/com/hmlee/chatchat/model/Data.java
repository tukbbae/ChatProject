package com.hmlee.chatchat.model;

import com.hmlee.chatchat.core.base.BaseObject;

public class Data extends BaseObject {

	private static final long serialVersionUID = -4332524419831666859L;

	private String senderEmail;
	private String senderName;
	private String message;
	
	public Data() {
		
	}
	
	public Data(String senderEmail, String senderName, String message) {
		this.senderEmail = senderEmail;
		this.senderName = senderName;
		this.message = message;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
