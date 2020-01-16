package com.project.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MagazineDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6370489035521867232L;
	private Long magazineId;
	private String ISSN;
	private String name;
	
	private List<ScienceAreaDto> scienceAreas;
}
