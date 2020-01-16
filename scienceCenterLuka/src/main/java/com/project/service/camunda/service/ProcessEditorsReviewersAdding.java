package com.project.service.camunda.service;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.NewMagazineFormEditorsReviewersResponseDto;
import com.project.dto.NewMagazineFormEditorsReviewersResponseRowDto;
import com.project.dto.NewMagazineFormResponseDto;
import com.project.model.EditorReviewerByScienceArea;
import com.project.model.Magazine;
import com.project.model.ScienceArea;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;

@Service
public class ProcessEditorsReviewersAdding implements JavaDelegate {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		try {
			NewMagazineFormResponseDto newMagazineDto = (NewMagazineFormResponseDto) execution.getVariable("newMagazineBasicInfo");

			Magazine magazine = unityOfWork.getMagazineRepository().getOne(newMagazineDto.getMagazineDbId());
			if(newMagazineDto.isUpdate()) {
				unityOfWork.getEditorReviewerByScienceAreaRepository().deleteOldEditorsReviewers(magazine);
			}
			
			NewMagazineFormEditorsReviewersResponseDto reviewrsEditorsDto = (NewMagazineFormEditorsReviewersResponseDto) execution.getVariable("newMagazineComplexInfo");
			List<NewMagazineFormEditorsReviewersResponseRowDto> rows = reviewrsEditorsDto.getRows();
			for(NewMagazineFormEditorsReviewersResponseRowDto row: rows) {
				ScienceArea sc = unityOfWork.getScienceAreaRepository().getOne(row.getScAreaId());
				UserSignedUp editor = unityOfWork.getUserSignedUpRepository().getOne(Long.parseLong(row.getEditorsId()));
				
				List<Long> ids = new ArrayList<Long>();
				row.getReviewersId().stream().forEach(i -> ids.add(Long.parseLong(i)));
				List<UserSignedUp> reviewers = unityOfWork.getUserSignedUpRepository().findAllById(ids);
				
				EditorReviewerByScienceArea editorByArea = EditorReviewerByScienceArea.builder()
						.editorReviewer(editor)
						.magazine(magazine)
						.scienceArea(sc)
						.editor(true)
						.build();
				
				unityOfWork.getEditorReviewerByScienceAreaRepository().save(editorByArea);
				
				
				for(UserSignedUp reviewer: reviewers) {
					EditorReviewerByScienceArea reviewerByArea = EditorReviewerByScienceArea.builder()
							.editorReviewer(reviewer)
							.magazine(magazine)
							.scienceArea(sc)
							.build();
					
					unityOfWork.getEditorReviewerByScienceAreaRepository().save(reviewerByArea);

				}
			}
		} catch (Exception e) {
			 throw new BpmnError("UnexpectedError", "UnexpectedfddError");

		}

	}

}
