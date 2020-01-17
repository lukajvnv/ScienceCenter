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
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.project.dto.NewUserResponseDto;
import com.project.dto.ReviewerConfirmationDto;
import com.project.dto.SignInDto;
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
	
	@GetMapping(path = "/register", produces = "application/json")
    public @ResponseBody FormFieldsDto get() {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("user_registration");

		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		for(FormField fp : properties) {
			System.out.println(fp.getId() + " " + fp.getType());
		}
		
        return new FormFieldsDto(task.getId(), pi.getId(), properties);
    }
	
	@PostMapping(path = "/register/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity post(@RequestBody NewUserResponseDto dto, @PathVariable String taskId) {
		
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
	
	@GetMapping(path = "/reviewerConfirmationStart/{taskId}", produces = "application/json")
    public @ResponseBody ReviewerConfirmationDto reviewerConfirmation( @PathVariable("taskId") String taskId) {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		ReviewerConfirmationDto response = (ReviewerConfirmationDto) runtimeService.getVariable(processInstanceId, "confirmationReviewerDto");
		// runtimeService.removeVariable(processInstanceId, "confirmationReviewerDto");
		
		
        return response;
    }
	
	@GetMapping(path = "/reviewerConfirmationEnd/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> reviewerConfirmation( @PathVariable("taskId") String taskId, @RequestParam("confirm") boolean confirm) {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
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
	
	@PostMapping(path = "/signIn", produces = "application/json")
    public @ResponseBody ResponseEntity<?> signIn(@RequestBody SignInDto signInDto) {
		User user = identityService.createUserQuery().userId(signInDto.getUsername()).singleResult();
		if(user == null) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		
		boolean ok = identityService.checkPassword(user.getId(), signInDto.getPassword());
		if(!ok) {
			
		}
		
	
		identityService.setAuthenticatedUserId(user.getId());
		// identityService.setAuthentication("demo", Arrays.asList(new String[]{"camunda-admin"}));
		String s= identityService.getCurrentAuthentication().getUserId(); //ako nema puca exception
		
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@GetMapping(path = "/signOut", produces = "application/json")
    public @ResponseBody ResponseEntity signOut() {
		
		identityService.clearAuthentication();

        return new ResponseEntity<>(HttpStatus.OK);
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
