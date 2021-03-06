package com.project.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.project.config.security.service.UserDetailsServiceImpl;
import com.project.dto.TaskDto;
import com.project.model.enums.Role;
import com.project.util.Response;
import com.project.util.TaskUrlEndpoint;

@RestController
@RequestMapping("/task")
@CrossOrigin
public class TaskController {
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private TaskUrlEndpoint taskUrlEndpoint;
	
	private final static String ADMIN_GROUP_ID = "camunda-admin";
	private final static String GUEST_GROUP_ID = "guest";
	private final static String REVIEWER_GROUP_ID = "reviewer";
	private final static String EDITOR_GROUP_ID = "editor";
	private final static String AUTHOR_GROUP_ID = "author";


	
	@GetMapping(path = "/assignedToUser/{processInstanceId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> get(@PathVariable String processInstanceId) {
		
//		UserSignedUp loggedUser = userDetailService.getLoggedUser();
//		if(loggedUser == null ) {
//			
//			return null;
//		}
		
		String username = "";
		try {
		   username = identityService.getCurrentAuthentication().getUserId(); //ako nema puca exception
		} catch (Exception e) {
			return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.CONFLICT), HttpStatus.CONFLICT);
		}

		
		List<Task> tasks = taskService.createTaskQuery().taskAssignee(username).active().list();
		
		Group adminGroup = identityService.createGroupQuery().groupMember(username).groupName("camunda BPM Administrators").singleResult();
		
		if(adminGroup != null) {
			tasks = taskService.createTaskQuery().active().list();
		}
		
//		if(loggedUser.getRole() == Role.ADMIN) {
//			tasks = taskService.createTaskQuery().active().list();
//		}
//		List<Task> tasks = taskService.createTaskQuery().active().list();

		
		List<TaskDto> dtos = new ArrayList<TaskDto>();
		for (Task task : tasks) {
			String taskDefinitionKey = task.getTaskDefinitionKey();
			// String url = taskUrlEndpoint.getValue(task.getTaskDefinitionKey());
			String[] params = taskUrlEndpoint.getParams(taskDefinitionKey);
			String url = params[0];
			String param = "";
			if(params.length > 1) {
				param = task.getId();
			}
			if(url == null) {};
			TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee(), task.getDescription(), taskDefinitionKey, task.getProcessInstanceId(), url, param);
			dtos.add(t);			
		}
		
        return new ResponseEntity<List<TaskDto>> (dtos,  HttpStatus.OK);
    }
	
	@GetMapping(path = "/removeTask/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable String taskId) throws URISyntaxException {
		
		boolean authorized = authorize(ADMIN_GROUP_ID);
		if(!authorized) {
			return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
		}
		

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		RestTemplate restTemplate = new RestTemplate();
		
		String uriBasic = "https://localhost:8085/rest/process-instance/{id}";
//		String uriBasic = "http://localhost:8085/rest/process-instance/{id}";
		URI uri = new URI(uriBasic.replace("{id}", processInstanceId));
		restTemplate.delete(uri);
		
		
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

}
