package com.project.service.camunda.service;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.integration.OrderIdDTO;
import com.project.service.KpService;

@Service
public class PaymentHandler implements JavaDelegate {
	
	@Autowired
	private KpService kpService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		// TODO: NA NC ZAVEDENO
		
		// TODO: KP PROCES PRETPLATE
		// Thread.sleep(10000);
		System.out.println("10s passed");
//		try {
//			// izazivanje greske
//			NewMagazineFormResponseDto newMagazineDto = null;
//			newMagazineDto.getMagazineDbId();
//
//		} catch (Exception e) {
//			 throw new BpmnError("PAYMENT_ERROR", "PAYMENT_ERROR");
//
//		}
		
		System.out.println(execution.getProcessInstanceId());
	
		OrderIdDTO orderDto = kpService.pay(execution.getProcessInstanceId());
		
		execution.setVariable("paymentInfo", orderDto);
	
	}

}
