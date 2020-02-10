package com.project.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MagazineEditionDto {

	private Long magazineEditionId;
	
	private Date publishingDate;
	
	private Float magazineEditionPrice;
	
	private String title;

}
