package com.hmlee.chatchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hmlee.chatchat.model.domain.Account;

import java.util.Optional;

/**
 * 사용자 계정 정보 접근을 위한 Repository Interface
 *
 * Created by hmlee
 */
@Repository
public interface AccountRepository extends JpaRepository <Account, String> {
    Optional<Account> findOneByEmail(String email);
}
