package com.project.util.camunda.validation;

import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;

public class MaxLength implements FormFieldValidator {

	@Override
	public boolean validate(Object submittedValue, FormFieldValidatorContext validatorContext) {
		// TODO Auto-generated method stub
		String value = (String) submittedValue;
		if (value.length() > 9) {
			return false;
		}
		return true;
	}

}
