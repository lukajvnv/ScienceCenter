package com.project.service.camunda.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ArticleProcessDto;
import com.project.dto.EditorReviewerByScienceAreaDto;
import com.project.dto.MagazineDto;
import com.project.dto.ScienceAreaDto;
import com.project.dto.UserDto;
import com.project.model.EditorReviewerByScienceArea;
import com.project.model.Magazine;
import com.project.model.ScienceArea;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;

@Service
public class RetrieveReviewers implements JavaDelegate {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) execution.getVariable("articleProcessDto");
		Magazine magazine = unityOfWork.getMagazineRepository().getOne(articleProcessDto.getMagazineId());
		
		List<EditorReviewerByScienceArea> reviewers = unityOfWork.getEditorReviewerByScienceAreaRepository().findByMagazineAndEditor(magazine, false);;
		List<EditorReviewerByScienceAreaDto> reviewersDto = new ArrayList<EditorReviewerByScienceAreaDto>();
		
		reviewers.stream().forEach(r -> {
			UserSignedUp u = r.getEditorReviewer();
			UserDto rDto = new UserDto(u.getUserId(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getCity(), u.getCountry(), u.getUserUsername(), u.getVocation());
			
			
			ScienceArea articleScienceArea = r.getScienceArea();
			ScienceAreaDto articleScienceAreaDto = new ScienceAreaDto(
					articleScienceArea.getScienceAreaId(), 
					articleScienceArea.getScienceAreaName(),
					articleScienceArea.getScienceAreaCode());
			
			Set<ScienceArea> scienceAreas = magazine.getScienceAreas();
			List<ScienceAreaDto> scienceAreasDto = new ArrayList<ScienceAreaDto>();
			scienceAreas.stream().forEach(sc -> {
				scienceAreasDto.add(new ScienceAreaDto(sc.getScienceAreaId(), sc.getScienceAreaName(), sc.getScienceAreaCode()));
			});
			
			MagazineDto mDto = new MagazineDto(magazine.getMagazineId(), magazine.getISSN(), magazine.getName(), scienceAreasDto);
			
			
			
			EditorReviewerByScienceAreaDto e = new EditorReviewerByScienceAreaDto(r.getEditorByScArId(), r.isEditor(), rDto, articleScienceAreaDto, mDto, u.getLongitude(), u.getLatitude());
			reviewersDto.add(e);
		});
		
		execution.setVariable("reviewersByScArea", reviewersDto);
	}

}
