package com.hmlee.chatchat.model.domain;

import org.hibernate.annotations.Type;

import com.hmlee.chatchat.core.base.BaseObject;

import javax.persistence.*;
import java.util.Date;

/**
 * Address
 *
 * Chat 주소록 Entity Model Class
 *
 * Created by hmlee
 */
@Entity
public class Address extends BaseObject {

    private static final long serialVersionUID = 7323163179211522198L;

    /*
        id 	bigint(20) 	NO 	PRI 	NULL 	auto_increment
        name 	varchar(20) 	NO 		NULL
        company 	varchar(20) 	NO 		NULL
        department 	varchar(20) 	NO 		NULL
        position 	varchar(20) 	NO 		NULL
        phone_number 	varchar(20) 	NO 		NULL
        email 	varchar(255) 	YES 		NULL
        regi_id 	varchar(200) 	YES 		NULL
        updated_date 	datetime 	NO 		2014-01-01 00:00:00
    */

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long idx;
    private String name;
    private String company;
    private String department;
    private String position;
    private String phoneNumber;
    private String email;
    private String regiId;
    private String deviceType;
    @Column(nullable = false)
    private Date updatedDate;

    public Address() {
    }

    public Address(String regiId, String phoneNumber, String deviceType) {
        this.regiId = regiId;
        this.phoneNumber = phoneNumber;
        this.deviceType = deviceType;
    }

    public Long getIdx() {
        return idx;
    }

    public void setIdx(Long idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegiId() {
        return regiId;
    }

    public void setRegiId(String regiId) {
        this.regiId = regiId;
    }

    public Date getUpdateDate() {
        return updatedDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updatedDate = updateDate;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
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
