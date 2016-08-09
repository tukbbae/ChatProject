package com.hmlee.chatchat.repository;

import java.util.List;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hmlee.chatchat.model.domain.User;

/**
 * Created by hmlee
 */
@Repository
public interface UserRepository extends DataTablesRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.email = :email")
    public User findUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.token = :token")
    public User findUserByToken(@Param("token") String token);

    @Query("SELECT u FROM User u WHERE u.name LIKE :name")
    public List<User> findUserByName(@Param("name") String name);
}
