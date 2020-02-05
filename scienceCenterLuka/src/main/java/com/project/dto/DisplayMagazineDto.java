package com.project.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.project.model.EditorReviewerByScienceArea;
import com.project.model.MagazineEdition;
import com.project.model.enums.WayOfPayment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisplayMagazineDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6370489035521867232L;
	private Long magazineId;
	private String ISSN;
	private String name;
	
	
	private List<ScienceAreaDto> scienceAreas;
	private WayOfPayment wayOfPayment;
	
	private Long membershipPrice;
	private UserDto chiefEditor;
	private List<EditorReviewerByScienceAreaDto> editors;
	private List<EditorReviewerByScienceAreaDto> reviewers;

	
//	private Set<MagazineEdition> magazineEditions;



}
