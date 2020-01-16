package com.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
	
	private String taskId;
	private String name;
	private String assignee;
	private String description;
	private String taskDefinitionId;
	
	private String processId;
	private String url;
	private String parameter;
}
