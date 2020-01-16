package com.project.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckingMagazineDataDto implements Serializable {
	
	
	
	/**
	 * 
	 */
	
	
	private static final long serialVersionUID = -7359754474797865889L;
	
	private String taskId;
	private DisplayMagazineDto magazine;
	private String comment;
	private boolean valid;

}
