package com.project.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewerConfirmationDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6901792238360701130L;
	
	private UserDto userDto;
	private List<ScienceAreaDto> scienceAreaDto;
}
