package com.hmlee.chatchat.model;

import com.hmlee.chatchat.core.base.BaseObject;

/**
 * Created by hmlee
 */
public class FcmRequestBody extends BaseObject {

	private static final long serialVersionUID = 1984652788178712358L;

	private String to;
	private Data data;
	
	public FcmRequestBody() {
		
	}
	
	public FcmRequestBody(String to, Data data) {
		this.to = to;
		this.data = data;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

}
