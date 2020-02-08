package com.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.FormFieldsDto;
import com.project.dto.FormSubmissionDto;
import com.project.dto.NewEditorDto;
import com.project.dto.NewUserResponseDto;
import com.project.dto.ReviewerConfirmationDto;
import com.project.dto.SignInDto;
import com.project.model.enums.Role;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;
import com.project.util.Response;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private UnityOfWork unityOfWork;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final static String ADMIN_GROUP_ID = "camunda-admin";
	private final static String GUEST_GROUP_ID = "guest";
	private final static String REVIEWER_GROUP_ID = "reviewer";
	private final static String EDITOR_GROUP_ID = "editor";
	private final static String AUTHOR_GROUP_ID = "author";
	
	@GetMapping(path = "/register", produces = "application/json")
    public @ResponseBody FormFieldsDto get() {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("user_registration");

		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		
        return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }
	
	@PostMapping(path = "/register/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> post(@RequestBody NewUserResponseDto dto, @PathVariable String taskId) {
		
		HashMap<String, Object> map = this.mapListToDto(dto.getFormFields());
		
		    // list all running/unsuspended instances of the process
//		    ProcessInstance processInstance =
//		        runtimeService.createProcessInstanceQuery()
//		            .processDefinitionKey("Process_1")
//		            .active() // we only want the unsuspended process instances
//		            .list().get(0);
		
//			Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list().get(0);
		
		
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		runtimeService.setVariable(processInstanceId, "registration", dto);
		formService.submitTaskForm(taskId, map);
		
		HistoricVariableInstanceEntity variable = (HistoricVariableInstanceEntity) historyService
				.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId)
				.variableName("error")
				.singleResult();
				
        if(variable != null) {
    		Response error = (Response) variable.getValue();
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, processInstanceId +" "+variable.getTextValue());
    		return ResponseEntity.status(error.getStatus()).body(error);
        }
		
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
//	@GetMapping(path = "/activation/{proccessId}/{hash}", produces = "application/json")
//    public @ResponseBody Object get( @PathVariable("proccessId") String processId, @PathVariable("hash") String hash) {
//		//provera da li korisnik sa id-jem pera postoji
//		//List<User> users = identityService.createUserQuery().userId("pera").list();
//		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).list().get(0);
//		Task tdask = taskService.createTaskQuery().processInstanceId(processId).list().stream().filter(t -> t.getId().equals("user_account_activation")).findFirst().get();
//
//		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
//		
//		TaskFormData tfd = formService.getTaskFormData(task.getId());
//		List<FormField> properties = tfd.getFormFields();
//		for(FormField fp : properties) {
//			System.out.println(fp.getId() + " " + fp.getType());
//		}
//		
//        return new Object();
//    }
	
	@GetMapping(path = "/activation/{proccessId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> get( @PathVariable("proccessId") String processId) {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).list().get(0);
		List<Task> stdask = taskService.createTaskQuery().processInstanceId(processId).list();
		Task tdask = taskService.createTaskQuery().processInstanceId(processId).list().stream().filter(t -> t.getTaskDefinitionKey().equals("user_account_activation")).findFirst().get();
		taskService.complete(tdask.getId());
		
		HistoricVariableInstanceEntity variable = (HistoricVariableInstanceEntity) historyService
				.createHistoricVariableInstanceQuery()
				.processInstanceId(processId)
				.variableName("error")
				.singleResult();
				
        if(variable != null) {
    		Response error = (Response) variable.getValue();
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, processInstanceId +" "+variable.getTextValue());
    		return ResponseEntity.status(error.getStatus()).body(error);
        }
		
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	// @PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/reviewerConfirmationStart/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> reviewerConfirmation( @PathVariable("taskId") String taskId) {
		
		boolean authorized = authorize(ADMIN_GROUP_ID);
		if(!authorized) {
			return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
		}
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		ReviewerConfirmationDto response = (ReviewerConfirmationDto) runtimeService.getVariable(processInstanceId, "confirmationReviewerDto");
		// runtimeService.removeVariable(processInstanceId, "confirmationReviewerDto");
		
		
        return new ResponseEntity<ReviewerConfirmationDto> (response, HttpStatus.OK);
    }
	
	// @PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/reviewerConfirmationEnd/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> reviewerConfirmation( @PathVariable("taskId") String taskId, @RequestParam("confirm") boolean confirm) {
		
		boolean authorized = authorize(ADMIN_GROUP_ID);
		if(!authorized) {
			return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
		}
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
	
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("accept_review_request", confirm);
		formService.submitTaskForm(task.getId(), properties);
		
		HistoricVariableInstanceEntity variable = (HistoricVariableInstanceEntity) historyService
				.createHistoricVariableInstanceQuery()
				.processInstanceId(task.getProcessInstanceId())
				.variableName("error")
				.singleResult();
				
        if(variable != null) {
    		Response error = (Response) variable.getValue();
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, processInstanceId +" "+variable.getTextValue());
    		return ResponseEntity.status(error.getStatus()).body(error);
        }
		
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/newEditor", produces = "application/json")
    public @ResponseBody ResponseEntity<?> newEditor(@RequestBody NewEditorDto newEditor) {
		
		boolean authorized = authorize(ADMIN_GROUP_ID);
		if(!authorized) {
			return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
		}
		
		UserSignedUp editor = UserSignedUp.builder()
				.firstName(newEditor.getFirstName())
				.lastName(newEditor.getLastName())
				.activatedAccount(true)
				.city(newEditor.getCity())
				.country(newEditor.getCountry())
				.email(newEditor.getEmail())
				.vocation(newEditor.getVocation())
				.role(Role.EDITOR)
				.wantToReviewe(true)
				.password(passwordEncoder.encode(newEditor.getPassword()))
				.userUsername(newEditor.getUsername()).build();
		
		unityOfWork.getUserSignedUpRepository().save(editor);
		
		User newUser =  identityService.newUser(newEditor.getUsername());
		newUser.setEmail(newEditor.getEmail());
		newUser.setFirstName(newEditor.getFirstName());
		newUser.setLastName(newEditor.getLastName());
		newUser.setPassword(newEditor.getPassword());
		identityService.saveUser(newUser);
		
		identityService.createMembership(newEditor.getUsername(), "editor");
		identityService.createMembership(newEditor.getUsername(), "reviewer");

		
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	private boolean authorize(String requestedGroupId) {
		String username = "";
		try {
		   username = identityService.getCurrentAuthentication().getUserId(); //ako nema puca exception
		} catch (Exception e) {
			return false;
		}
		
		Group group = identityService.createGroupQuery().groupMember(username).groupId(requestedGroupId).singleResult();
		
		if(group != null) {
			return true;
		} else {
			return false;
		}
	}
	
	private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormSubmissionDto temp : list){
			map.put(temp.getFieldId(), temp.getFieldValue());
		}
		
		return map;
	}
	

}
