package com.hmlee.chatchat.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.hmlee.chatchat.core.base.BaseObject;

/**
 * Friend
 *
 * Chat 친구목록 Entity Model Class
 *
 * Created by hmlee
 */
@Entity(name = "FRIEND_LIST")
public class Friend extends BaseObject {

	/**
	 * User serialVersionUID
	 */
	private static final long serialVersionUID = -7809757772842847225L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "USER_EMAIL", nullable = false)
	private String userEmail;

	@ManyToOne
	@JoinColumn(name = "FRIEND_ID")
	private User friend;

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}

}
