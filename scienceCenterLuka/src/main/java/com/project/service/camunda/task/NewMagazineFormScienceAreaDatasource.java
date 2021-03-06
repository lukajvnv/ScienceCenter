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
import com.project.model.Magazine;
import com.project.repository.UnityOfWork;

@Service
public class NewMagazineFormScienceAreaDatasource implements TaskListener {
	
	@Autowired
	IdentityService identityService;
	
	@Autowired
	FormService formService;
	
	@Autowired
	UnityOfWork unityOfWork;

	@Override
	public void notify(DelegateTask delegateTask) {
		
		NewMagazineFormResponseDto newMagazineDto = (NewMagazineFormResponseDto) delegateTask.getExecution().getVariable("newMagazineBasicInfo");
		if ( newMagazineDto != null ) {
			Magazine magazine = unityOfWork.getMagazineRepository().getOne(newMagazineDto.getMagazineDbId());
			return;
		}
		
		  TaskFormData formData =  formService.getTaskFormData(delegateTask.getId());
		  FormField field = formData.getFormFields().stream().filter(f -> f.getId().equals("science_area")).findFirst().get();
		  EnumFormType ft = (EnumFormType)field.getType();
		  Map<String, String> map = ft.getValues();
		  
		  unityOfWork.getScienceAreaRepository().findAll().forEach(scArea -> { 
			  map.put(scArea.getScienceAreaId().toString(), scArea.getScienceAreaCode() + ":" + scArea.getScienceAreaName());
		  });
	}
}
