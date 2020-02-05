package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.model.Article;
import com.project.repository.UnityOfWork;

@Service
public class UpdateFileChanges implements JavaDelegate {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		byte[] file = (byte[]) execution.getVariable("change_file");
		ArticleProcessDto articleProcess = (ArticleProcessDto) execution.getVariable("articleProcessDto");
	
		Article a = unityOfWork.getArticleRepository().getOne(articleProcess.getArticleId());
		a.setFile(file);
		
		unityOfWork.getArticleRepository().save(a);
	}

}
