package com.project.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArticleDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 7267623785811514588L;
	
	private NewArticleRequestDto newArticleRequestDto;
	private NewArticleResponseDto newAarticleResponseDto;
	
	private String comment;
}
