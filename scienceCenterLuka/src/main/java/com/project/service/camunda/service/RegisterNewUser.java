package com.project.service.camunda.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.dto.NewUserResponseDto;
import com.project.config.security.service.UserDetailsServiceImpl;
import com.project.dto.FormSubmissionDto;
import com.project.exception.SignUpVerificationException;
import com.project.model.Magazine;
import com.project.model.ScienceArea;
import com.project.model.enums.Role;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;

@Service
public class RegisterNewUser implements JavaDelegate {
	
	@Autowired
	IdentityService identityService;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	private UnityOfWork unityOfWork;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	public void execute(DelegateExecution execution) throws Exception, SignUpVerificationException {
		// TODO Auto-generated method stub
		try {
			String processId = execution.getProcessInstanceId();
			NewUserResponseDto registrationDto = (NewUserResponseDto) execution.getVariable("registration");
			
			List<Long> scAreasId = new ArrayList<Long>();
			registrationDto.getScienceAreaId().forEach(scId -> {
				scAreasId.add(Long.parseLong(scId));
			});
			
			List<ScienceArea> selectedScienceAreas = unityOfWork.getScienceAreaRepository().findAllById(scAreasId);
			
			List<FormSubmissionDto> fields = registrationDto.getFormFields();
			Map<String, Object> map = new HashMap<String, Object>();
			fields.forEach(f -> {
				map.put(f.getFieldId(), f.getFieldValue());
			});
			
			List<User> usersWithSameId = identityService.createUserQuery().userId((String)map.get("username")).list();
			if( usersWithSameId.size() > 0 ) {
				execution.setVariable("verified", false);
				com.project.util.Response response = new com.project.util.Response("User whit this name already exists", HttpStatus.UNAUTHORIZED);
				execution.setVariable("error", response);

				return;
			}
			
			UserSignedUp newSignUpUser = UserSignedUp.builder()
					.userUsername((String)map.get("username"))
					.password(passwordEncoder.encode((String)map.get("password")))
					.role(Role.COMMON_USER)
					.wantToReviewe((Boolean)map.get("is_reviewer"))
					.userScienceAreas(new HashSet<ScienceArea>(selectedScienceAreas))
					.vocation((String)map.get("title"))
					.firstName((String)map.get("first_name"))
					.lastName((String)map.get("last_name"))
					.country((String)map.get("country"))
					.city((String)map.get("city"))
					.email((String)map.get("email"))
					.build();
			
			unityOfWork.getUserSignedUpRepository().save(newSignUpUser);
			
			User newUser =  identityService.newUser((String)map.get("username"));
			newUser.setEmail((String)map.get("email"));
			newUser.setFirstName((String)map.get("first_name"));
			newUser.setLastName((String)map.get("last_name"));
			newUser.setPassword((String)map.get("password"));
			identityService.saveUser(newUser);
			
			identityService.createMembership((String)map.get("username"), "author");
			
			execution.setVariable("verified", true);
			
			execution.setVariable("user", newUser.getId());
			
		
		} catch (Exception e) {
			 throw new BpmnError("UnexpectedError", "UnexpectedfddError");
		}
	}

}
