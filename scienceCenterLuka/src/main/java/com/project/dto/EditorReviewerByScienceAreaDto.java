package com.project.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditorReviewerByScienceAreaDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 802818058310832320L;

	private Long editorByScArId;
	
	private boolean editor;
	
	private UserDto editorReviewer;
	
	private ScienceAreaDto scienceArea;
	
	private MagazineDto magazine;
	
	private Double longitude;
	private Double latitude;
}
