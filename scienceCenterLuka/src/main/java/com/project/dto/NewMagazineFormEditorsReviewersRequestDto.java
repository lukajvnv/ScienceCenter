package com.project.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewMagazineFormEditorsReviewersRequestDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -650307090012278025L;
	private List<ScienceAreaDto> scienceAreas;
	private List<ReviewerEditorDto> reviewers;
	private List<ReviewerEditorDto> editors;
	
	private String taskId;
	private String processId;
}
