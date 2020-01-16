package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.enums.Role;
import com.project.model.user.UserActivationCode;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;

@Service
public class ProcessReviewerConfirmation implements JavaDelegate {
	
	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		try {
			UserActivationCode code = (UserActivationCode) execution.getVariable("activation_code");

			boolean accept = (boolean) execution.getVariable("accept_review_request");
			
			if(accept) {
				UserSignedUp user = unityOfWork.getUserSignedUpRepository().findByUserUsername(code.getUserId());
				user.setRole(Role.REVIEWER);
				unityOfWork.getUserSignedUpRepository().save(user);
			}
		} catch (Exception e) {
			 throw new BpmnError("UnexpectedError", "UnexpectedfddError");

		}
	}

}
