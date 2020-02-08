package com.project.service.camunda.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.NewUserResponseDto;
import com.project.dto.FormSubmissionDto;
import com.project.model.user.UserActivationCode;
import com.project.repository.UnityOfWork;
import com.project.util.Base64Utility;
import com.project.util.DateConverter;

@Service
public class GenerateHash implements JavaDelegate{
	
	@Autowired
	private UnityOfWork unityOfWork;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		try {
			NewUserResponseDto registrationDto = (NewUserResponseDto) execution.getVariable("registration");
					
			List<FormSubmissionDto> fields = registrationDto.getFormFields();
			Map<String, Object> map = new HashMap<String, Object>();
			fields.forEach(f -> {
				map.put(f.getFieldId(), f.getFieldValue());
			});
			
			String date = DateConverter.encodeT(new Timestamp(System.currentTimeMillis()));
			String textToHash = map.get("username") + date + map.get("password");
			
			UserActivationCode code = new UserActivationCode();
			code.setUserId((String)map.get("username"));
			code.setCode(hash(textToHash));
			code.setEmail((String)map.get("email"));
			
			execution.setVariable("activation_code", code);
		} catch (Exception e) {
			 throw new BpmnError("UnexpectedError", "UnexpectedfddError");

		}
	}
	
	private String hash(String data) {
		//Kao hes funkcija koristi SHA-256
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			byte[] dataHash = sha256.digest(data.getBytes());
			return Base64Utility.encode(dataHash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
