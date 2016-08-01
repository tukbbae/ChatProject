package com.hmlee.chatchat.model;

import org.hibernate.validator.constraints.NotEmpty;

import com.hmlee.chatchat.core.constant.AccountRole;

import javax.validation.constraints.NotNull;

/**
 * AccountCreateForm
 *
 * 사용자 계정 생성을 위한 폼 Class
 *
 * Created by hmlee
 */
public class AccountCreateForm {

    @NotEmpty
    private String username = "";

    @NotEmpty
    private String password = "";

    @NotEmpty
    private String email = "";

    @NotEmpty
    private String confirmPassword = "";

    @NotNull
    private AccountRole role = AccountRole.ROLE_AGENT;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public AccountRole getRole() {
        return role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }
}
