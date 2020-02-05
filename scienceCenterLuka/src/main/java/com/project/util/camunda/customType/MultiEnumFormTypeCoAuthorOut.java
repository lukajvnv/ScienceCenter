package com.project.util.camunda.customType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.impl.form.type.SimpleFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.ObjectValueImpl;
import org.camunda.bpm.engine.variable.value.TypedValue;

import com.project.dto.UserDto;

// NECE RADITI OVO 
//public class MultiEnumFormTypeCoAuthorOutSistem extends EnumFormType {
//	
//	//protected Map<String, String> myValues;
//	
//	public final static String TYPE_NAME = "multiEnumCoAuthorOutSystem";
//
//	
//	protected Map<String, UserDto> values = new HashMap<String, UserDto>();
//	
//	@Override
//	public String getName() {
//		// TODO Auto-generated method stub
//		return TYPE_NAME;
//	}
//
//
//	public MultiEnumFormTypeCoAuthorOutSistem(Map<String, String> values) {
//		super(values);
//		//this.values = values;
//	}
//	
//	
//
//	@Override
//	protected void validateValue(Object value) {
//		// TODO Auto-generated method stub
//		if(value != null) {
////		      if(values != null && !values.containsKey(value)) {
////		        throw new ProcessEngineException("Invalid value for enum form property: " + value);
////		      }
////			if(values != null ) {
////		        throw new ProcessEngineException("Invalid value for enum form property: " + value);
////		      }
//		 }
//	}
//
//
//	@Override
//	public TypedValue convertValue(TypedValue propertyValue) {
//		// TODO Auto-generated method stub
//		Object value = propertyValue.getValue();
////	    if(value == null || String.class.isInstance(value)) {
////	      validateValue(value);
////	      //Variables.untypedValue(value)
////	      //Variables.untypedValue(value, isTransient)
////	      return Variables.stringValue((String) value, propertyValue.isTransient());
////	    }
////	    else {
////	      throw new ProcessEngineException("Value '"+value+"' is not of type String.");
////	    }
//		if(value == null || ArrayList.class.isInstance(value)) {
//		      validateValue(value);
//		      // return Variables.objectValue((ArrayList<String>) value, propertyValue.isTransient());
//		      return new ObjectValueImpl((ArrayList<?>) value, propertyValue.isTransient());
//		    }
//		    else {
//		      throw new ProcessEngineException("Value '"+value+"' is not of type String.");
//		    }
//	}
//
//
//
//
//	@Override
//	public Object getInformation(String key) {
//		// TODO Auto-generated method stub
//		if (key.equals("values")) {
//		      return values;
//		    }
//		    return null;
//	}
//
//
//
//	@Override
//	public Map<String, String> getValues() {
//		// TODO Auto-generated method stub
//		return values;
//	}
//
//
//
//	@Override
//	public Object convertFormValueToModelValue(Object propertyValue) {
//		// TODO Auto-generated method stub
//		validateValue(propertyValue);
//	    return propertyValue;
//	}
//
//
//
//	@Override
//	public String convertModelValueToFormValue(Object modelValue) {
//		// TODO Auto-generated method stub
//		if(modelValue != null) {
////		      if(!(modelValue instanceof String)) {
////		        throw new ProcessEngineException("Model value should be a String");
////		      }
//			if(!(modelValue instanceof ArrayList<?>)) {
//		        throw new ProcessEngineException("Model value should be a String");
//		      }
//		      validateValue(modelValue);
//		    }
//		    return (String) modelValue;
//	}
//
//}

public class MultiEnumFormTypeCoAuthorOut extends SimpleFormFieldType {
	
	public final static String TYPE_NAME = "multiEnumCoAuthorOut";

	
	protected Map<String, UserDto> myValues = new HashMap<String, UserDto>();
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return TYPE_NAME;
	}
	
	
	public TypedValue convertValue(TypedValue propertyValue) {
		Object value = propertyValue.getValue();
		if(value == null || ArrayList.class.isInstance(value)) {
	      validateValue(value);
	      // return Variables.objectValue((ArrayList<String>) value, propertyValue.isTransient());
	      return new ObjectValueImpl((ArrayList<UserDto>) value, propertyValue.isTransient());
	    }
	    else {
	      throw new ProcessEngineException("Value '"+value+"' is not of type String.");
	    }
	}

	  protected void validateValue(Object value) {
//	    if(value != null) {
//	      if(myValues != null && !myValues.containsKey(value)) {
//	        throw new ProcessEngineException("Invalid value for enum form property: " + value);
//	      }
//	    }
		  
	  }

	@Override
	public TypedValue convertToFormValue(TypedValue propertyValue) {
		// TODO Auto-generated method stub
		return super.convertToFormValue(propertyValue);
	}

	@Override
	public TypedValue convertToModelValue(TypedValue propertyValue) {
		// TODO Auto-generated method stub
		return super.convertToModelValue(propertyValue);
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
		      if(!(modelValue instanceof String)) {
		        throw new ProcessEngineException("Model value should be a String");
		      }
		      validateValue(modelValue);
		    }
		    return (String) modelValue;
	}

	@Override
	public Object getInformation(String key) {
		// TODO Auto-generated method stub
		if (key.equals("values")) {
		      return myValues;
		    }
		    return null;
	}

	public Map<String, UserDto> getValues() {
	    return myValues;
	  }
	

}


