package com.project.util.camunda.validation;

import java.util.Map;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;

public class TimeToRespondValidator implements FormFieldValidator {

	@Override
	public boolean validate(Object submittedValue, FormFieldValidatorContext validatorContext) {
		// TODO Auto-generated method stub
		
		Map<String,Object> completeSubmit = validatorContext.getSubmittedValues();
		
		Integer day = (Integer) completeSubmit.get("reviewing_deadline_day");
		Integer minute = (Integer) completeSubmit.get("reviewing_deadline_minute");
		Integer hour = (Integer) completeSubmit.get("reviewing_deadline_hour");

		if(day == 0 && hour == 0 && minute == 0) {
			return false;
		}
		
		return true;
	}

}
