package com.project.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewMagazineFormEditorsReviewersResponseRowDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -650307090012278025L;
	
	private Long scAreaId;
	private List<String> reviewersId;
	private String editorsId;
	
	
	private String taskId;
	private String processId;
}
