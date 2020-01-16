package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.NewMagazineFormResponseDto;
import com.project.model.Magazine;
import com.project.repository.UnityOfWork;

@Service
public class ActivateMagazine implements JavaDelegate {
	
	@Autowired
	private UnityOfWork unityOfWork;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
	
		try {
			NewMagazineFormResponseDto newMagazineDto = (NewMagazineFormResponseDto) execution.getVariable("newMagazineBasicInfo");
			Magazine magazine = unityOfWork.getMagazineRepository().getOne(newMagazineDto.getMagazineDbId());
			magazine.setActive(true);
			
			unityOfWork.getMagazineRepository().save(magazine);
		} catch (Exception e) {
			 throw new BpmnError("UnexpectedError", "UnexpectedfddError");

		}
	}

}
