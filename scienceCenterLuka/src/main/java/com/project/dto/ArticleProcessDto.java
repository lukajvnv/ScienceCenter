package com.project.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.project.model.OpinionAboutArticle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleProcessDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7910694131881060169L;
	private String processId;
	private Long magazineId;
	private Long articleId;
	
	private String author;
	private String whoIsChiefEditor;
	private String whoIsScienceEditor;

	private List<String> reviewers ;
	
	private List<OpinionAboutArticle> opinions;
	
	private List<OpinionAboutArticle> editorOpinions;
	
	private List<OpinionAboutArticle> authorsMessages;

	
	private int iteration;
}
