package com.project.service.camunda.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleDto;
import com.project.dto.ArticleProcessDto;
import com.project.dto.ScienceAreaDto;
import com.project.dto.TermDto;
import com.project.dto.UserDto;
import com.project.model.Article;
import com.project.model.ScienceArea;
import com.project.model.Term;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;

@Service
public class NewArticleAnalizeArticleDataInitializationi implements TaskListener {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		DelegateExecution execution = delegateTask.getExecution();
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");

		Article article = unityOfWork.getArticleRepository().getOne(articleProcessDto.getArticleId());
		
		ScienceArea sc = article.getScienceArea();
		ScienceAreaDto scienceAreaDto = new ScienceAreaDto(sc.getScienceAreaId(), sc.getScienceAreaName(), sc.getScienceAreaCode());
		
		UserSignedUp author = article.getAuthor();
		UserDto authorDto = new UserDto(author.getUserId(), 
				 author.getFirstName(), author.getLastName(), 
				 author.getEmail(), author.getCity(), 
				 author.getCountry(), author.getUserUsername(), author.getVocation());
		
		Set<UserSignedUp> coAuthors = article.getCoAuthors();
		List<UserDto> coAuthorsDto = new ArrayList<UserDto>();
		coAuthors.stream().forEach(c -> {
			coAuthorsDto.add(new UserDto(c.getUserId(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getCity(), c.getCountry(), c.getUserUsername(), c.getVocation()));
		});
		
		Set<Term> terms = article.getKeyTerms();		
		List<TermDto> termsDto = new ArrayList<TermDto>();
		terms.stream().forEach(t -> {
			termsDto.add( new TermDto(t.getTermId(), t.getTermName()));
		});
		
		ArticleDto articleDto = new ArticleDto(delegateTask.getId(), 
				delegateTask.getProcessInstanceId(), 
				article.getArticleId(), 
				article.getArticleTitle(), 
				article.getArticleAbstract(), 
				scienceAreaDto, 
				article.getPublishingDate(), 
				authorDto, 
				coAuthorsDto, 
				termsDto, 
				article.getArticlePrice(), null);
		
		execution.setVariable("articleRequestDto", articleDto);
	}

}
