package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.integration.OrderIdDTO;
import com.project.model.Magazine;
import com.project.model.MagazineEdition;
import com.project.repository.UnityOfWork;
import com.project.service.KpService;

@Service
public class PaymentHandler implements JavaDelegate {
	
	@Autowired
	private KpService kpService;
	
	@Autowired
	private UnityOfWork unityOfWork;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		// TODO: NA NC ZAVEDENO
		
		// TODO: KP PROCES PRETPLATE
		// Thread.sleep(10000);
//		try {
//			// izazivanje greske
//			NewMagazineFormResponseDto newMagazineDto = null;
//			newMagazineDto.getMagazineDbId();
//
//		} catch (Exception e) {
//			 throw new BpmnError("PAYMENT_ERROR", "PAYMENT_ERROR");
//
//		}
		
		try {
			Long magazineId = (Long) execution.getVariable("select_magazine_id");
			Magazine magazine = unityOfWork.getMagazineRepository().getOne(magazineId);
			
			MagazineEdition edition = unityOfWork.getMagazineEditionRepository().findByMagazineOrderByPublishingDateDesc(magazine).get(0);
			
			System.out.println(execution.getProcessInstanceId());

			OrderIdDTO orderDto = kpService.pay(execution.getProcessInstanceId(), edition);
			
			execution.setVariable("paymentInfo", orderDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 throw new BpmnError("PAYMENT_ERROR", "PAYMENT_ERROR");
		}
	
	}

}
