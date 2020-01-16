package com.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.project.model.user.UserSignedUp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class EditorReviewerByScienceArea {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long editorByScArId;
	
	@Column
	private boolean editor;
	
	@ManyToOne
	@JoinColumn(name = "editor_reviewer_id")
	private UserSignedUp editorReviewer;
	
	@ManyToOne
	@JoinColumn(name = "science_area_id")
	private ScienceArea scienceArea;
	
	@ManyToOne
	@JoinColumn(name = "magazine_id")
	private Magazine magazine;

}
