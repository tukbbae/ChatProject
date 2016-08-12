package com.hmlee.chatchat.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by hmlee
 */
public class FCMBody implements Serializable {

	public Map<String, String> to;
	public Map<String, String> data;

	public void setTo(String token) {
		if (to == null)
			to = new HashMap<>();

		to.put("to", token);
	}

	public void createData(String senderEmail, String senderName, String message) {
		if (data == null)
			data = new HashMap<>();

		data.put("senderEmail", senderEmail);
		data.put("senderName", senderName);
		data.put("message", message);
	}

}
