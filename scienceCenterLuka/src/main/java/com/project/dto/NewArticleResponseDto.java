package com.project.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewArticleResponseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8848348204449356145L;
	
	
	
	private String articleTitle;
	private String articleAbstract;
	private String articleScienceArea;
	private Float articlePrice;
	private List<TermDto> articleTerm;
	private List<UserDto> articleCoAuthors;
    
	private String fileName;
	private String file;
	
}
