package com.hmlee.chatchat.model.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.hmlee.chatchat.core.base.BaseObject;

/**
 * User
 *
 * Chat 사용자 Entity Model Class
 *
 * Created by hmlee
 */
@Entity
public class User extends BaseObject {

	/**
	 * User serialVersionUID
	 */
	private static final long serialVersionUID = -5085184304924341385L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String email;

	private String name;

	private String token;

	private String device_type;

	@Column(nullable = false)
	private Date updatedDate;

	public User() {

	}

	public User(String email, String name, String token, String device_type) {
		this.email = email;
		this.name = name;
		this.token = token;
		this.device_type = device_type;
	}

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
	
    @PrePersist
    public void prePersist() {
        updatedDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = new Date();
    }

}
