package com.project.service.camunda.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.NewMagazineFormResponseDto;
import com.project.model.Magazine;
import com.project.model.ScienceArea;
import com.project.model.enums.WayOfPayment;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;

@Service
public class ProcessNewMagazineData implements JavaDelegate {
	
	@Autowired
	IdentityService identityService;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	private UnityOfWork unityOfWork;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		try {
			NewMagazineFormResponseDto newMagazineDto = (NewMagazineFormResponseDto) execution.getVariable("newMagazineBasicInfo");

			List<Long> scAreasId = new ArrayList<Long>();
			newMagazineDto.getScience_area().forEach(scId -> {
				scAreasId.add(Long.parseLong(scId));
			});
			
			List<ScienceArea> selectedScienceAreas = unityOfWork.getScienceAreaRepository().findAllById(scAreasId);

			String userId = (String) execution.getVariable("user");
			UserSignedUp chiefEditor = unityOfWork.getUserSignedUpRepository().findByUserUsername(userId);

			Magazine newMagazine = Magazine.builder()
					.ISSN(newMagazineDto.getIssn_number())
					.membershipPrice(newMagazineDto.getMembership_price())
					.name(newMagazineDto.getName())
					.wayOfPayment(WayOfPayment.valueOf(newMagazineDto.getPayment_option()))
					.scienceAreas(new HashSet<ScienceArea>(selectedScienceAreas))
					.chiefEditor(chiefEditor)
					.build();
			
			if(newMagazineDto.getMagazineDbId() != null) {
				newMagazine.setMagazineId(newMagazineDto.getMagazineDbId());
				unityOfWork.getMagazineRepository().save(newMagazine);
			} else {
				Magazine savedMagazine = unityOfWork.getMagazineRepository().save(newMagazine);
				newMagazineDto.setMagazineDbId(savedMagazine.getMagazineId());
				execution.setVariable("newMagazineBasicInfo", newMagazineDto);
			}
		} catch (Exception e) {
			 throw new BpmnError("UnexpectedError", "UnexpectedfddError");

		}
	}

}
