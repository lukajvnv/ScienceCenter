package com.project.dto;

import java.io.Serializable;

public class ScienceAreaDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1359878929207693310L;

	private Long scienceAreaId;
	
	private String scienceAreaName;
	
	private String scienceAreaCode;
	
	

	public ScienceAreaDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ScienceAreaDto(Long scienceAreaId, String scienceAreaName, String scienceAreaCode) {
		super();
		this.scienceAreaId = scienceAreaId;
		this.scienceAreaName = scienceAreaName;
		this.scienceAreaCode = scienceAreaCode;
	}

	public Long getScienceAreaId() {
		return scienceAreaId;
	}

	public void setScienceAreaId(Long scienceAreaId) {
		this.scienceAreaId = scienceAreaId;
	}

	public String getScienceAreaName() {
		return scienceAreaName;
	}

	public void setScienceAreaName(String scienceAreaName) {
		this.scienceAreaName = scienceAreaName;
	}

	public String getScienceAreaCode() {
		return scienceAreaCode;
	}

	public void setScienceAreaCode(String scienceAreaCode) {
		this.scienceAreaCode = scienceAreaCode;
	}
	
	

}
