package com.hmlee.chatchat.model;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import com.hmlee.chatchat.core.constant.AccountRole;
import com.hmlee.chatchat.model.domain.Account;

/**
 * Created by hmlee
 */
public class CurrentAccount extends User {

    private Account account;

    public CurrentAccount(Account account) {
        super(account.getUsername(), account.getPassword(), AuthorityUtils.createAuthorityList(account.getRoleCode()));
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public String getUsername() {
        return account.getUsername();
    }

    public AccountRole getAccountRole() {
        return AccountRole.getInstance(account.getRoleCode());
    }
}
