package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewerFilterDto {

	private boolean scienceAreaFilter;
	private boolean geoFilter;
}
