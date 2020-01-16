package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.model.Article;
import com.project.model.DoiGenerator;
import com.project.repository.DoiGeneratorRepository;
import com.project.repository.UnityOfWork;

@Service
public class GenerateDoi implements JavaDelegate {
	
	@Autowired
	private UnityOfWork unityOfWork;
	
	@Autowired
	private DoiGeneratorRepository doiGeneratorRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		Article article = unityOfWork.getArticleRepository().getOne(articleProcessDto.getArticleId());
		
		
		DoiGenerator d = new DoiGenerator();
		d.setArticle(article);
		DoiGenerator dSaved = doiGeneratorRepository.save(d);
		
		String doi = generateDoi(dSaved);
		article.setDoi(doi);
		unityOfWork.getArticleRepository().save(article);
		
	}
	
	private String generateDoi(DoiGenerator d) {
		Integer b = d.getGeneratedId();
		String bString = b.toString();
		StringBuilder builder = new StringBuilder("10.1").append(bString).insert(7, '/');
		
		return builder.toString();
	}

}
