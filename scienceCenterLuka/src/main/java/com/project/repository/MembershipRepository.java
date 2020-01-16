package com.project.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.Magazine;
import com.project.model.user.tx.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
	
	Membership findByMagazineAndEndAtBefore(Magazine magazine, Date now);
}
