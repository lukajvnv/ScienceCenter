package com.project.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddReviewersDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1643781589019222610L;
	
	private ArticleDto articleDto;
	private MagazineDto magazineDto;
	private List<EditorReviewerByScienceAreaDto> editorsReviewersDto;
	
	private String subProcessMfExecutionId;
	private boolean insideMf;

}
