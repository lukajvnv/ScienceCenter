package com.project.service.camunda.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.NewMagazineFormEditorsReviewersRequestDto;
import com.project.dto.NewMagazineFormResponseDto;
import com.project.dto.ReviewerEditorDto;
import com.project.dto.ScienceAreaDto;
import com.project.model.Magazine;
import com.project.model.ScienceArea;
import com.project.model.enums.Role;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;

@Service
public class AddEditorsReviewersDataSource implements TaskListener {
	
	@Autowired
	private UnityOfWork unityOfWork;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		System.out.println("Forma za recezente");
		
		DelegateExecution execution = delegateTask.getExecution();
		NewMagazineFormResponseDto newMagazineDto = (NewMagazineFormResponseDto) execution.getVariable("newMagazineBasicInfo");
		
		Magazine magazine = unityOfWork.getMagazineRepository().getOne(newMagazineDto.getMagazineDbId());
		
		UserSignedUp chiefEditor = magazine.getChiefEditor();
	    Set<ScienceArea> scienceAreas = magazine.getScienceAreas();
	    
	    List<UserSignedUp> reviewers = unityOfWork.getUserSignedUpRepository().findByRoleOrderByLastName(Role.REVIEWER);
	    List<UserSignedUp> editors = unityOfWork.getUserSignedUpRepository().findByRoleOrderByLastName(Role.EDITOR);
	    editors.remove(chiefEditor);
	    
	    List<ScienceAreaDto> scAreasDto = new ArrayList<ScienceAreaDto>();
	    List<ReviewerEditorDto> reviewersDto =  new ArrayList<ReviewerEditorDto>();
	    List<ReviewerEditorDto> editorsDto =  new ArrayList<ReviewerEditorDto>();
	    
	    scienceAreas.stream().forEach(sC -> scAreasDto.add(new ScienceAreaDto(sC.getScienceAreaId(), sC.getScienceAreaName(), sC.getScienceAreaCode())));
//	    reviewers.stream().forEach(r -> reviewersDto.add(new ReviewerEditorDto(r.getUserId(), r.getFirstName() + " " + r.getLastName())));
//	    editors.stream().forEach(e -> editorsDto.add(new ReviewerEditorDto(e.getUserId(), e.getFirstName() + " " + e.getLastName())));

	    reviewers.stream().forEach(r -> {
		    List<ScienceAreaDto> scAreasDtoR = new ArrayList<ScienceAreaDto>();

		    r.getUserScienceAreas().stream().forEach(sC -> scAreasDtoR.add(new ScienceAreaDto(sC.getScienceAreaId(), sC.getScienceAreaName(), sC.getScienceAreaCode())));

	    	reviewersDto.add(new ReviewerEditorDto(r.getUserId(), r.getFirstName() + " " + r.getLastName(), scAreasDtoR));
	    });
	    editors.stream().forEach(e -> { 
	    	List<ScienceAreaDto> scAreasDtoR = new ArrayList<ScienceAreaDto>();
	    	e.getUserScienceAreas().stream().forEach(sC -> scAreasDto.add(new ScienceAreaDto(sC.getScienceAreaId(), sC.getScienceAreaName(), sC.getScienceAreaCode())));
	    	
	    	editorsDto.add(new ReviewerEditorDto(e.getUserId(), e.getFirstName() + " " + e.getLastName(), scAreasDtoR));
	    });
	    
	   
	    NewMagazineFormEditorsReviewersRequestDto dto = new NewMagazineFormEditorsReviewersRequestDto(scAreasDto, reviewersDto, editorsDto, delegateTask.getId(), delegateTask.getProcessInstanceId());
	    execution.setVariable("editorReviewersRequest", dto);
	}

}
