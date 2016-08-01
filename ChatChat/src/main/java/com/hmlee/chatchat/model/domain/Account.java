package com.hmlee.chatchat.model.domain;

import org.hibernate.annotations.Type;

import com.hmlee.chatchat.core.base.BaseObject;
import com.hmlee.chatchat.core.constant.AccountRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.util.Date;

/**
 * 웹 계정 Model Class
 *
 * Created by hmlee
 */
@Entity
public class Account extends BaseObject {

    private static final long serialVersionUID = 7325302924813867790L;

    @Id
    private String username;
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String roleCode;
    private String restrictCode;
    private Date activateDate;
    private int loginFailCount;
    private Date passwordChangeDate;
    private Date lastLoginDate;
    @Column(nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isPasswordChanged;
    private Date registerDate;

    public Account() {
    }

    public Account(String username, String password, String roleCode) {
        this.username = username;
        this.password = password;
        this.roleCode = roleCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleCode() {
        return AccountRole.getInstance(roleCode).getRoleCode();
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = AccountRole.getInstance(roleCode).getRoleCode();
    }

    public String getRestrictCode() {
        return restrictCode;
    }

    public void setRestrictCode(String restrictCode) {
        this.restrictCode = restrictCode;
    }

    public Date getActivateDate() {
        return activateDate;
    }

    public void setActivateDate(Date activateDate) {
        this.activateDate = activateDate;
    }

    public int getLoginFailCount() {
        return loginFailCount;
    }

    public void setLoginFailCount(int loginFailCount) {
        this.loginFailCount = loginFailCount;
    }

    public Date getPasswordChangeDate() {
        return passwordChangeDate;
    }

    public void setPasswordChangeDate(Date passwordChangeDate) {
        this.passwordChangeDate = passwordChangeDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public boolean isPasswordChanged() {
        return isPasswordChanged;
    }

    public void setIsPasswordChanged(boolean isPasswordChanged) {
        this.isPasswordChanged = isPasswordChanged;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    @PrePersist
    public void prePersist() {
        registerDate = new Date();
    }

}
