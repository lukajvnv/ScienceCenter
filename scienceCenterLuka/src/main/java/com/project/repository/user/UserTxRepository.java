package com.project.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.user.tx.UserTx;

public interface UserTxRepository extends JpaRepository<UserTx, Long> {

	UserTx findBykPClientIdentifier(long kpIdentifier);
	UserTx findByOrderId(long orderId);

}
