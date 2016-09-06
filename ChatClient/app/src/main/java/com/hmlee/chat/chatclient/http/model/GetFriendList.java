package com.hmlee.chat.chatclient.http.model;

import com.google.api.client.util.Key;

/**
 * Created by hmlee on 2016-09-02.
 */
public class GetFriendList {
    @Key
    String userEmail;

    public GetFriendList(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
