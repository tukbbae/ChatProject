package com.hmlee.chat.chatclient.http.model;

import com.google.api.client.util.Key;

import java.util.ArrayList;

/**
 * Created by hmlee on 2016-09-06.
 */
public class GetFriendListResponse {

    @Key
    private ArrayList<User> friendList;

    public ArrayList<User> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<User> friendList) {
        this.friendList = friendList;
    }
}
