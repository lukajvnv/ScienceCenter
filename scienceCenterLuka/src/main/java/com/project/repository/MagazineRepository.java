package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.model.Magazine;


//@Repository
public interface MagazineRepository extends JpaRepository<Magazine, Long> {

}
