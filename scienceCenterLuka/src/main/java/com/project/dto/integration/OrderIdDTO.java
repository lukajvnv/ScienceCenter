package com.project.dto.integration;

import java.io.Serializable;

public class OrderIdDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7509791125253124361L;

	private Long orderId;
	
	private String kpUrl;
	
	private String taskId;

	public OrderIdDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OrderIdDTO(Long orderId) {
		super();
		this.orderId = orderId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	public String getKpUrl() {
		return kpUrl;
	}

	public void setKpUrl(String kpUrl) {
		this.kpUrl = kpUrl;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	
}
