package com.project.service.camunda.task;

import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.NewMagazineFormResponseDto;
import com.project.dto.integration.OrderIdDTO;
import com.project.model.Magazine;
import com.project.repository.UnityOfWork;

@Service
public class KpPaymentInit implements TaskListener {
	
	@Override
	public void notify(DelegateTask delegateTask) {
		
		OrderIdDTO orderIdDto = (OrderIdDTO) delegateTask.getExecution().getVariable("paymentInfo");
		orderIdDto.setTaskId(delegateTask.getId());
		
		delegateTask.getExecution().setVariable("paymentInfo", orderIdDto);
		
		//delegateTask.getExecution().setVariable("kpTaskId", delegateTask.getId());
	}
}
