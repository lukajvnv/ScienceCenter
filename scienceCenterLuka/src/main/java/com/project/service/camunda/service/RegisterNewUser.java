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
			Map<String, String> map = new HashMap<String, String>();
			fields.forEach(f -> {
				map.put(f.getFieldId(), (String)f.getFieldValue());
			});
			
			List<User> usersWithSameId = identityService.createUserQuery().userId(map.get("username")).list();
			if( usersWithSameId.size() > 0 ) {
				execution.setVariable("verified", false);
				com.project.util.Response response = new com.project.util.Response("User whit this name already exists", HttpStatus.UNAUTHORIZED);
				execution.setVariable("error", response);

				return;
			}
			
			UserSignedUp newSignUpUser = UserSignedUp.builder()
					.userUsername(map.get("username"))
					.password(passwordEncoder.encode(map.get("password")))
					.role(Role.COMMON_USER)
					.wantToReviewe(Boolean.parseBoolean(map.get("is_reviewer")))
					.userScienceAreas(new HashSet<ScienceArea>(selectedScienceAreas))
					.vocation(map.get("title"))
					.firstName(map.get("first_name"))
					.lastName(map.get("last_name"))
					.country(map.get("country"))
					.city(map.get("city"))
					.email(map.get("email"))
					.build();
			
			unityOfWork.getUserSignedUpRepository().save(newSignUpUser);
			
			User newUser =  identityService.newUser(map.get("username"));
			newUser.setEmail(map.get("email"));
			newUser.setFirstName(map.get("first_name"));
			newUser.setLastName(map.get("last_name"));
			newUser.setPassword(map.get("password"));
			identityService.saveUser(newUser);
			
			execution.setVariable("verified", true);
			
			execution.setVariable("user", newUser.getId());
			
			

			
//			identityService.setAuthentication("demo", Arrays.asList(new String[]{"camunda-admin"}));
//			String s= identityService.getCurrentAuthentication().getUserId();
//			boolean ok = identityService.checkPassword("", "");
//			identityService.clearAuthentication();
//			String s1= identityService.getCurrentAuthentication().getUserId();
		} catch (Exception e) {
			 throw new BpmnError("UnexpectedError", "UnexpectedfddError");
		}
	}

}
