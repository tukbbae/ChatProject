package com.hmlee.chatchat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hmlee.chatchat.model.CurrentAccount;
import com.hmlee.chatchat.model.domain.Account;

import java.util.ArrayList;
import java.util.List;

/**
 * AccountDetailsService
 *
 * Spring Security에서 제공하는 DB 기반 사용자 인증 모듈을 사용하기 위한 Service Class
 *
 * Created by hmlee
 */
@Service
public class AccountDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AccountDetailsService.class);

    private final AccountService accountService;

    @Autowired
    public AccountDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Autowired
    private MessageSource messageSource;

    @Override
    public CurrentAccount loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountService.getAccountByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Account with username=%s was not found", username)));

        logger.debug("account => {}", account);

        return new CurrentAccount(account);
    }
/*
    public List<GrantedAuthority> getAuthorities(String roleCode) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        AccountRole accountRole = AccountRole.getInstance(roleCode);

        if (accountRole == AccountRole.ROLE_ADMIN) {
            authorities.add(new SimpleGrantedAuthority(AccountRole.ROLE_ADMIN.getRoleCode()));
        } else if (accountRole == AccountRole.ROLE_OPERATOR) {
            authorities.add(new SimpleGrantedAuthority(AccountRole.ROLE_OPERATOR.getRoleCode()));
        } else {
            authorities.add(new SimpleGrantedAuthority(AccountRole.ROLE_AGENT.getRoleCode()));
        }

        logger.debug("authorities => {}", authorities);
        return authorities;
    }

    private Account getAccount(String username) {
        Account account = accountRepository.findOne(username);
        logger.debug("account => {}", account);
        return account;
    }
*/
}
