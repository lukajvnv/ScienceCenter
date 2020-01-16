package com.project.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.project.model.EditorReviewerByScienceArea;
import com.project.model.Magazine;
import com.project.model.ScienceArea;

public interface EditorReviewerByScienceAreaRepository extends JpaRepository<EditorReviewerByScienceArea, Long> {

	@Modifying
	@Transactional
	@Query("DELETE FROM EditorReviewerByScienceArea f WHERE f.magazine = ?1")
	void deleteOldEditorsReviewers(Magazine magazine);
	
	List<EditorReviewerByScienceArea> findByMagazine(Magazine magazine);
	List<EditorReviewerByScienceArea> findByMagazineAndEditor(Magazine magazine, boolean editor);

	
	EditorReviewerByScienceArea findByMagazineAndScienceAreaAndEditor(Magazine magazine, ScienceArea scArea, boolean editor);
}
