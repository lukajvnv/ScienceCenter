package com.project.util.camunda.customType;

import java.util.ArrayList;
import java.util.Arrays;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.form.type.SimpleFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.ObjectValueImpl;
import org.camunda.bpm.engine.variable.value.TypedValue;

import com.project.dto.UserDto;

public class FileFormType extends SimpleFormFieldType {
	
	public final static String TYPE_NAME = "file";


	protected TypedValue convertValue(TypedValue propertyValue) {
		// TODO Auto-generated method stub
		Object value = propertyValue.getValue();
		if(value == null || value.getClass().isArray()) {
	      validateValue(value);
	      // return Variables.byteArrayValue((byte[]) value, propertyValue.isTransient());
	      return new ObjectValueImpl((byte[]) value, propertyValue.isTransient());

	    }
	    else {
	      throw new ProcessEngineException("Value '"+value+"' is not of type byte[].");
	    }
	      // return new ObjectValueImpl((byte[]) value, propertyValue.isTransient());

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return TYPE_NAME;
	}
	
	protected void validateValue(Object value) {
		
		  
	 }

	@Override
	public Object convertFormValueToModelValue(Object propertyValue) {
		// TODO Auto-generated method stub
		validateValue(propertyValue);
	    return propertyValue;
	}

	@Override
	public String convertModelValueToFormValue(Object modelValue) {
		// TODO Auto-generated method stub
		if(modelValue != null) {
//		      if(!(modelValue instanceof String)) {
//		        throw new ProcessEngineException("Model value should be a String");
//		      }
		      validateValue(modelValue);
		    }
		return (String) modelValue;
	}

//	@Override
//	public TypedValue convertToFormValue(TypedValue propertyValue) {
//		// TODO Auto-generated method stub
//		return super.convertToFormValue(propertyValue);
//	}
//
//	@Override
//	public TypedValue convertToModelValue(TypedValue propertyValue) {
//		// TODO Auto-generated method stub
//		return super.convertToModelValue(propertyValue);
//	}

}
