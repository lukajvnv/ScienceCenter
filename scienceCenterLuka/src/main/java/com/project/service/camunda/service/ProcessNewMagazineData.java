package com.project.service.camunda.service;

import java.util.ArrayList;
import java.util.Arrays;
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
			String multiIds = (String) execution.getVariable("science_areaMulti");
//			newMagazineDto.getScience_area().forEach(scId -> {
//				scAreasId.add(Long.parseLong(scId));
//			});
			Arrays.asList(multiIds.split(":")).forEach(scId -> {
				scAreasId.add(Long.parseLong(scId));
			}); ;
			
			List<ScienceArea> selectedScienceAreas = unityOfWork.getScienceAreaRepository().findAllById(scAreasId);

			String userId = (String) execution.getVariable("user");
			UserSignedUp chiefEditor = unityOfWork.getUserSignedUpRepository().findByUserUsername(userId);
			
			String name = (String) execution.getVariable("name");
			String issn_number = (String) execution.getVariable("issn_number");
			String payment_option = (String) execution.getVariable("payment_option");
			Long membership_price = (Long) execution.getVariable("membership_price");



			Magazine newMagazine = Magazine.builder()
					.ISSN(issn_number)
					.membershipPrice(membership_price)
					.name(name)
					.wayOfPayment(WayOfPayment.valueOf(payment_option))
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
