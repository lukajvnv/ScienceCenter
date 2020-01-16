package com.project.dto;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4801992639345803841L;
	private Long termId;
	private String termName;

}
