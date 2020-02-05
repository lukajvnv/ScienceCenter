package com.project.util.camunda.validation;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;

public class EmailValidator implements FormFieldValidator {

	@Override
	public boolean validate(Object submittedValue, FormFieldValidatorContext validatorContext) {
		// TODO Auto-generated method stub
		
		if(submittedValue == null) {
			return false;
		}
		
		String email = (String) submittedValue;
	    String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

		return email.matches(regex);
	}

}
