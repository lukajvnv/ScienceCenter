package com.project.service.camunda.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.ReviewerConfirmationDto;
import com.project.dto.ScienceAreaDto;
import com.project.dto.UserDto;
import com.project.model.ScienceArea;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;

@Service
public class ReviewerConfirmationDataInitialization implements TaskListener {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub

		DelegateExecution execution = delegateTask.getExecution();
		String userId = (String) execution.getVariable("user");
		
		UserSignedUp user = unityOfWork.getUserSignedUpRepository().findByUserUsername(userId);
		
		UserDto userDto = new UserDto(user.getUserId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getCity(), user.getCountry(), user.getUserUsername(), user.getVocation());
		
		Set<ScienceArea> scienceAreas = user.getUserScienceAreas();
		List<ScienceAreaDto> scienceAreasDto = new ArrayList<ScienceAreaDto>();
		scienceAreas.stream().forEach(s -> {
			scienceAreasDto.add(new ScienceAreaDto(s.getScienceAreaId(), s.getScienceAreaName(), s.getScienceAreaCode()));
		});
		
		ReviewerConfirmationDto reviewerConfirmationDto = new ReviewerConfirmationDto(userDto, scienceAreasDto);
		
		execution.setVariable("confirmationReviewerDto", reviewerConfirmationDto);
	}

}
