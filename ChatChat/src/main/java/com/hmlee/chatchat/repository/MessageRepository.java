package com.hmlee.chatchat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hmlee.chatchat.model.domain.Message;

/**
 * Created by hmlee
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
