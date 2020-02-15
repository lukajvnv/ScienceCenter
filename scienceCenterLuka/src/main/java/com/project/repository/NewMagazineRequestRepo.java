package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.session.NewMagazineRequestSession;

public interface NewMagazineRequestRepo extends JpaRepository<NewMagazineRequestSession, Long> {

	NewMagazineRequestSession findByMagazineId(long id);
}
