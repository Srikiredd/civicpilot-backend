package com.civicpilot.repository;

import com.civicpilot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
		// TODO Auto-generated method stub
		
	

    boolean existsByEmail(String email);
}