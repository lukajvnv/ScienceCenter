package com.project.service.camunda.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.AddReviewersDto;
import com.project.dto.ArticleDto;
import com.project.dto.ArticleProcessDto;
import com.project.dto.EditorReviewerByScienceAreaDto;
import com.project.dto.MagazineDto;
import com.project.dto.ScienceAreaDto;
import com.project.dto.UserDto;
import com.project.model.Article;
import com.project.model.Magazine;
import com.project.model.ScienceArea;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;

@Service
public class AddReviewerForArticleDataInitialization implements TaskListener {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub

		DelegateExecution execution = delegateTask.getExecution();
		
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		Article article = unityOfWork.getArticleRepository().getOne(articleProcessDto.getArticleId());
		Magazine magazine = unityOfWork.getMagazineRepository().getOne(articleProcessDto.getMagazineId());
		UserSignedUp author = unityOfWork.getUserSignedUpRepository().findByUserUsername(articleProcessDto.getAuthor());
		Set<UserSignedUp> coAuthors = article.getCoAuthors();
		List<UserDto> coAuthorsDto = new ArrayList<UserDto>();
		
		coAuthors.stream().forEach(c -> {
			coAuthorsDto.add(new UserDto(c.getUserId(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getCity(), c.getCountry(), c.getUserUsername(), c.getVocation()));
		});
		
		ScienceArea scienceArea = article.getScienceArea();
		ScienceAreaDto scienceAreaDto = new ScienceAreaDto(scienceArea.getScienceAreaId(), scienceArea.getScienceAreaName(), scienceArea.getScienceAreaCode());
		
		UserDto authorDto = new UserDto(author.getUserId(), author.getFirstName(), author.getLastName(), author.getEmail(), author.getCity(), author.getCountry(), author.getUserUsername(), author.getVocation());
		
		ArticleDto articleDto = new ArticleDto(delegateTask.getId(), execution.getProcessInstanceId(), article.getArticleId(), article.getArticleTitle(), article.getArticleAbstract(), 
				scienceAreaDto, article.getPublishingDate(), authorDto, coAuthorsDto, null, null, null);
		
		Set<ScienceArea> scienceAreas = magazine.getScienceAreas();
		List<ScienceAreaDto> scienceAreasDto = new ArrayList<ScienceAreaDto>();
		scienceAreas.stream().forEach(sc -> {
			scienceAreasDto.add(new ScienceAreaDto(sc.getScienceAreaId(), sc.getScienceAreaName(), sc.getScienceAreaCode()));
		});
		
		MagazineDto magazineDto = new MagazineDto(magazine.getMagazineId(), magazine.getISSN(), magazine.getName(), scienceAreasDto);
		
		List<EditorReviewerByScienceAreaDto> reviewersDto = (ArrayList<EditorReviewerByScienceAreaDto>) execution.getVariable("reviewersByScArea");
		
		AddReviewersDto addReviewersDto = new AddReviewersDto(articleDto, magazineDto, reviewersDto, "", false);
		execution.setVariable("addReviewersDto", addReviewersDto);
	}

}
