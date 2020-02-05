package com.project.util.camunda.validation;

import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidator;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorContext;

public class CustomValidatorWithDetails implements FormFieldValidator {
	
	public CustomValidatorWithDetails() {
	    System.out.println("CREATED");
	  }

	  

	@Override
	public boolean validate(Object submittedValue, FormFieldValidatorContext validatorContext) {

	    // get access to the current execution
//	    DelegateExecution e = validatorContext.getExecution();
//
//	    // get access to all form fields submitted in the form submit
//	    Map<String,Object> completeSubmit = validatorContext.getSubmittedValues();
		

		
//		if (submittedValue == null) {
//		      return true;
//		    }
//
//		    if (submittedValue.equals("A") || submittedValue.equals("B")) {
//		      return true;
//		    }
//
//		    if (submittedValue.equals("C")) {
//		      // instead of returning false, use an exception to specify details about
//		      // what went wrong
//		      throw new FormFieldValidationException("EXPIRED", "Unable to validate " + submittedValue);
//		    }
//
//		    // return false in the generic case
//		    return false;
			return true;
		}
	
}
