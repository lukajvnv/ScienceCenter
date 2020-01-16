package com.project.service.camunda.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpOr;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.dto.NewArticleRequestDto;
import com.project.dto.NewArticleResponseDto;
import com.project.dto.ScienceAreaDto;
import com.project.dto.TermDto;
import com.project.dto.UpdateArticleChangesDto;
import com.project.dto.UpdateArticleDto;
import com.project.dto.UserDto;
import com.project.model.Article;
import com.project.model.Magazine;
import com.project.model.OpinionAboutArticle;
import com.project.model.ScienceArea;
import com.project.model.Term;
import com.project.model.enums.ArticleStatus;
import com.project.model.enums.ReviewingType;
import com.project.model.enums.Role;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;
import com.project.util.Base64Utility;

@Service
public class NewArticleUpdateFormChangesDataInitialization implements TaskListener {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub

		DelegateExecution execution = delegateTask.getExecution();
		String proccessId = delegateTask.getProcessInstanceId();
		String taskId = delegateTask.getId();
				
		Long magazineId = (Long) execution.getVariable("select_magazine_id");
		Magazine magazine = unityOfWork.getMagazineRepository().getOne(magazineId);
		List<ScienceArea> scienceAreas = new ArrayList<ScienceArea>(magazine.getScienceAreas());
		
		List<UserSignedUp> coAuthors = unityOfWork.getUserSignedUpRepository().findByRoleOrderByLastName(Role.COMMON_USER);
		String userId = (String) execution.getVariable("user");
		UserSignedUp authorInitiator = unityOfWork.getUserSignedUpRepository().findByUserUsername(userId);
		coAuthors.remove(authorInitiator);
		
		List<Term> terms = unityOfWork.getTermRepository().findAll();

		List<ScienceAreaDto> scienceAreasDto = new ArrayList<ScienceAreaDto>();
		List<TermDto> termsDto = new ArrayList<TermDto>();
		List<UserDto> coAuthorsDto = new ArrayList<UserDto>();
		
		scienceAreas.stream().forEach(sc -> scienceAreasDto.add(new ScienceAreaDto(sc.getScienceAreaId(), sc.getScienceAreaName(), sc.getScienceAreaCode())));
		terms.stream().forEach(t -> termsDto.add(new TermDto(t.getTermId(), t.getTermName())));
		coAuthors.stream().forEach(c -> coAuthorsDto.add(new UserDto(c.getUserId(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getCity(), c.getCountry(), c.getUserUsername(), c.getVocation())));
	
		NewArticleRequestDto requestDto = new NewArticleRequestDto(taskId, proccessId, scienceAreasDto, termsDto, coAuthorsDto);

		
		ArticleProcessDto articleDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		Article oldSavedArticle = unityOfWork.getArticleRepository().getOne(articleDto.getArticleId());
		
		String decodedFile = Base64Utility.encode(oldSavedArticle.getFile());
		Set<Term> termsArticle = oldSavedArticle.getKeyTerms();
		Set<UserSignedUp> coAuthorsArticle  = oldSavedArticle.getCoAuthors();
		
		List<TermDto> termsArticleDto = new ArrayList<TermDto>();
		List<UserDto> coAuthorsArticleDto = new ArrayList<UserDto>();
		
		termsArticle.stream().forEach(t -> termsArticleDto.add(new TermDto(t.getTermId(), t.getTermName())));
		coAuthorsArticle.stream().forEach(c -> coAuthorsArticleDto.add(new UserDto(c.getUserId(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getCity(), c.getCountry(), c.getUserUsername(), c.getVocation())));
		
		NewArticleResponseDto responseDto = new NewArticleResponseDto(
				oldSavedArticle.getArticleTitle(), oldSavedArticle.getArticleAbstract(), 
				Long.toString(oldSavedArticle.getScienceArea().getScienceAreaId()), oldSavedArticle.getArticlePrice(), 
				termsArticleDto, coAuthorsArticleDto, 
				oldSavedArticle.getFileFormat(), decodedFile);
		
		String authorId = (String) execution.getVariable("user");
		
		OpinionAboutArticle opinion = new OpinionAboutArticle(articleDto.getArticleId(), authorId, ReviewingType.AUTHOR_RESPONSE, ArticleStatus.ACCEPTED, "", "", articleDto.getIteration());

		UpdateArticleChangesDto updateArticleChangesDto = new UpdateArticleChangesDto(requestDto, responseDto, articleDto.getOpinions(), articleDto.getEditorOpinions(), opinion);
		
		execution.setVariable("updateArticleChangesDto", updateArticleChangesDto);
	}

}
