package com.project.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewerEditorDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2959224626700339865L;
	private Long id;
	private String name;
	private List<ScienceAreaDto> scAreaInterests;

	
}
