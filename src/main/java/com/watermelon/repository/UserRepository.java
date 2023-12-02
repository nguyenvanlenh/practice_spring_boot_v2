package com.watermelon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watermelon.model.entity.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findByEmailAndFirstName(String email, String firstName);
	User findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	
}
