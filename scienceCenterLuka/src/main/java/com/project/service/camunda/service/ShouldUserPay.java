package com.project.service.camunda.service;

import java.util.Date;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.Magazine;
import com.project.model.user.UserSignedUp;
import com.project.model.user.tx.Membership;
import com.project.repository.UnityOfWork;

@Service
public class ShouldUserPay implements JavaDelegate {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		Long magazineId = (Long) execution.getVariable("select_magazine_id");
		Magazine magazine = unityOfWork.getMagazineRepository().getOne(magazineId);
		
		String userUsername = (String) execution.getVariable("user");
		UserSignedUp user = unityOfWork.getUserSignedUpRepository().findByUserUsername(userUsername);
		
		Membership membership = unityOfWork.getMembershipRepository().findByMagazineAndEndAtBefore(magazine, new Date());
		boolean shouldPay = true;
		if( membership != null ) {
			shouldPay = false;
		}
		
		execution.setVariable("should_pay", shouldPay);
	}

}
