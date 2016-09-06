package com.hmlee.chat.chatclient.http.model;

import com.google.api.client.util.Key;

/**
 * Created by hmlee on 2016-09-06.
 */
public class User {

    @Key
    private String email;

    @Key
    private String name;

    @Key
    private String token;

    @Key
    private String device_type;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }
}
