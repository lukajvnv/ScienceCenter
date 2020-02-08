package com.project.service.camunda.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
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
public class AddReviewerForArticleAdditionalDataInitialization implements TaskListener {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Autowired
	private FormService formService;
	
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
		
		MagazineDto magazineDto = new MagazineDto(magazine.getMagazineId(), magazine.getISSN(), magazine.getName(), magazine.getWayOfPayment(), scienceAreasDto);
		
		List<EditorReviewerByScienceAreaDto> reviewersDto = (ArrayList<EditorReviewerByScienceAreaDto>) execution.getVariable("reviewersByScArea");
		
		// ***********
		reviewersDto = filterReviewers(execution, articleProcessDto, reviewersDto);
		
		// reviewers enum datasource init
		TaskFormData formData =  formService.getTaskFormData(delegateTask.getId());
		FormField field = formData.getFormFields().stream().filter(f -> f.getId().equals("additional_reviewer")).findFirst().get();
		EnumFormType ft = (EnumFormType)field.getType();
		Map<String, String> map = ft.getValues();
		
		reviewersDto.forEach(rev -> { 
			ScienceAreaDto scDto = rev.getScienceArea();
			UserDto userDto = rev.getEditorReviewer();
			String text = new StringBuilder(userDto.getFirstName())
					.append(" ")
					.append(userDto.getLastName())
					.append(", ")
					.append(userDto.getCity())
					.append(", scienceArea: ")
					.append(scDto.getScienceAreaCode())
					.append(" : ")
					.append(scDto.getScienceAreaName())
					.toString();
		  map.put(userDto.getUserUsername(), text);
		});
		

		AddReviewersDto addReviewersDto = new AddReviewersDto(articleDto, magazineDto, reviewersDto, "", false, null, null);
		execution.setVariable("addReviewersDto", addReviewersDto);
	}
	
	private List<EditorReviewerByScienceAreaDto> filterReviewers(DelegateExecution execution, ArticleProcessDto articleProcessDto, List<EditorReviewerByScienceAreaDto> initialList){
		
		List<EditorReviewerByScienceAreaDto> retVal = new ArrayList<EditorReviewerByScienceAreaDto>(initialList);
		
		List<EditorReviewerByScienceAreaDto> listToRemove = new ArrayList<EditorReviewerByScienceAreaDto>();
		List<String> existingReviewerIds = articleProcessDto.getReviewers();
		
		existingReviewerIds.forEach(id -> {
			EditorReviewerByScienceAreaDto existingRev =  retVal.stream().filter(rev -> rev.getEditorReviewer().getUserUsername().equals(id)).findFirst().orElse(null);
			if(existingRev != null) {
				listToRemove.add(existingRev);
			}
		});
		
		retVal.removeAll(listToRemove);
		
		return retVal;
	}

}
