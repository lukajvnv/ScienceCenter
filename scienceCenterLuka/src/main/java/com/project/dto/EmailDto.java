package com.project.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDto implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -2529601554087041840L;
	
	private String subject;
	private String content;
	private String messageType;
	private String sendTo;
	private String sendFrom;
	
	private boolean multipart;
	private String encoding;

}
