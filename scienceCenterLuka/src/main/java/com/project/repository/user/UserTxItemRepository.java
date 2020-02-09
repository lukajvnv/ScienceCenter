package com.project.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.user.tx.UserTxItem;

public interface UserTxItemRepository extends JpaRepository<UserTxItem, Long> {

}
