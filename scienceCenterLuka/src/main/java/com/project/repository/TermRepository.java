package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.Term;

public interface TermRepository extends JpaRepository<Term, Long> {

}
