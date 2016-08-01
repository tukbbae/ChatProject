package com.hmlee.chatchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hmlee.chatchat.core.base.BaseService;
import com.hmlee.chatchat.model.AccountCreateForm;
import com.hmlee.chatchat.model.domain.Account;
import com.hmlee.chatchat.repository.AccountRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by hmlee
 */
@Service
public class AccountService extends BaseService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * 사용자명으로 사용자 정보 조회
     *
     * @param username 사용자명
     * @return Java 8에서 제공한는 Optional 타입 : Null 과 NPE(NullPointerException)을 회피하기 위해 사용되는 타입
     *          - <a href="http://www.oracle.com/technetwork/articles/java/java8-optional-2175753.html">java8-optional-2175753</a>
     *          - <a href="http://starplatina.tistory.com/entry/Java-8-Optional">http://starplatina.tistory.com/entry/Java-8-Optional</a>
     *          - <a href="http://javaiyagi.tistory.com/443">http://javaiyagi.tistory.com/443</a>
     */
    public Optional<Account> getAccountByUsername(String username) {
        return Optional.ofNullable(accountRepository.findOne(username));
    }

    public Optional<Account> getAccountByEmail(String email) {
        return accountRepository.findOneByEmail(email);
    }

    public Collection<Account> getAllAccounts() {
        return accountRepository.findAll(new Sort("username"));
    }

    public Account createAccount(AccountCreateForm form) {
        Account account = new Account();
        account.setEmail(form.getEmail());
        account.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        account.setRoleCode(form.getRole().getRoleCode());
        return accountRepository.save(account);
    }
}
