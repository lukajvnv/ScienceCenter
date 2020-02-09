package com.project.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.enums.Role;
import com.project.model.user.UserSignedUp;

public interface UserSignedUpRepository extends JpaRepository<UserSignedUp, Long> {

	UserSignedUp findByUserUsername(String username);
	
	List<UserSignedUp> findByRoleOrderByLastName(Role r);
}
