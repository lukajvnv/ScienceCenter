package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.ScienceArea;

public interface ScienceAreaRepository extends JpaRepository<ScienceArea, Long> {

}
