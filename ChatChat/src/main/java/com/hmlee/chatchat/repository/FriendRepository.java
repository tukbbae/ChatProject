package com.hmlee.chatchat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hmlee.chatchat.model.domain.Friend;
import com.hmlee.chatchat.model.domain.User;

/**
 * 사용자 friend 정보 접근을 위한 Repository Interface
 *
 * Created by hmlee
 */
@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

	@Query("SELECT f FROM FRIEND_LIST f WHERE f.userEmail = :userEmail")
	public List<User> findFriendListByUserId(@Param("userEmail") String userEmail);
}
