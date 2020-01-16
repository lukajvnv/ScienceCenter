package com.project.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewMagazineFormEditorsReviewersResponseDto implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 397691395789694412L;
	
	private List<NewMagazineFormEditorsReviewersResponseRowDto> rows;
	
}
	
