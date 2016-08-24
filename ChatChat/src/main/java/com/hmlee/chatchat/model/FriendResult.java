package com.hmlee.chatchat.model;

import java.util.ArrayList;

import com.hmlee.chatchat.core.base.BaseObject;
import com.hmlee.chatchat.model.domain.User;

public class FriendResult extends BaseObject {

	private static final long serialVersionUID = -6706376585398822112L;
	
    private ArrayList<User> friendList;

	public ArrayList<User> getFriendList() {
		return friendList;
	}

	public void setFriendList(ArrayList<User> friendList) {
		this.friendList = friendList;
	}
    
    

}
