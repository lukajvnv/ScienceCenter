package com.project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorException;
import org.camunda.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.project.dto.CheckingMagazineDataDto;
import com.project.dto.FormFieldsDto;
import com.project.dto.FormSubmissionDto;
import com.project.dto.MagazineDto;
import com.project.dto.NewMagazineFormEditorsReviewersRequestDto;
import com.project.dto.NewMagazineFormEditorsReviewersResponseDto;
import com.project.dto.NewMagazineFormRequestDto;
import com.project.dto.NewMagazineFormResponseDto;
import com.project.dto.ScienceAreaDto;
import com.project.dto.integration.NewClientResponse;
import com.project.model.Magazine;
import com.project.model.ScienceArea;
import com.project.repository.ArticleRepository;
import com.project.repository.MagazineRepository;
import com.project.repository.UnityOfWork;
import com.project.service.MagazineService;
import com.project.util.Response;

@RestController
@RequestMapping("/magazine")
@CrossOrigin
public class MagazineController {

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
	MagazineService magazineService;
	
	@Value("${server.port}")
	private String webShopClientport;
	
	private final static String ADMIN_GROUP_ID = "camunda-admin";
	private final static String GUEST_GROUP_ID = "guest";
	private final static String REVIEWER_GROUP_ID = "reviewer";
	private final static String EDITOR_GROUP_ID = "editor";
	private final static String AUTHOR_GROUP_ID = "author";
	
	private HttpEntity<?> createHeader (Object body){
		HttpHeaders headers = new HttpHeaders();
		headers.add("external", "true");
		headers.add("hostsc", "localhost:" + webShopClientport);
		
		HttpEntity<?> entity;
		if(body != null) {
			entity = new HttpEntity<>(body, headers);
		} else {
			entity = new HttpEntity<>(headers);
		}
		
		return entity;
	}
	
	@GetMapping(path = "/addMagazineToKp/{taskId}", produces = "application/json")
    public  ResponseEntity<?> retrieveEditorsReviewers( @PathVariable String taskId) {
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		NewMagazineFormResponseDto newMagazineDto = (NewMagazineFormResponseDto) runtimeService.getVariable(processInstanceId, "newMagazineBasicInfo");
		long magId = newMagazineDto.getMagazineDbId();
		
		String newClientRequestUrl = "https://localhost:8762/requestHandler/client/newClient/";
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = null;
		try {
			response = restTemplate.exchange(newClientRequestUrl + magId, HttpMethod.GET, createHeader(null), String.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		
		return new ResponseEntity<>(new NewClientResponse(response.getBody()), HttpStatus.OK);
    }
	
	@GetMapping(path = "/start", produces = "application/json")
    public @ResponseBody NewMagazineFormRequestDto get() {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("new_magazine");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		
//		FormField name = properties.stream().filter(f1 -> f1.getId().equals("name")).findFirst().get();
//		FormField issn_number = properties.stream().filter(f2 -> f2.getId().equals("issn_number")).findFirst().get();
//		FormField payment_option = properties.stream().filter(f3 -> f3.getId().equals("payment_option")).findFirst().get();
//		FormField membership_price = properties.stream().filter(f4 -> f4.getId().equals("membership_price")).findFirst().get();
//		FormField science_area_name = properties.stream().filter(f5 -> f5.getId().equals("science_area")).findFirst().get();

	
		
        return new NewMagazineFormRequestDto(task.getId(), pi.getId(), properties, "");
    }
	
	@GetMapping(path = "/retrieveEditorsReviewers", produces = "application/json")
    public @ResponseBody FormFieldsDto retrieveEditorsReviewers() {
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
	
	@PostMapping(path = "/submitBasicInfo/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> post(@RequestBody NewMagazineFormResponseDto dto, @PathVariable String taskId) {
		
		
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		// Map<String, Object> map = fromDtoToMap(dto);
		Map<String, Object> map = mapListToDto(dto.getFormFields(), processInstanceId);

		runtimeService.setVariable(processInstanceId, "newMagazineBasicInfo", dto);
		try {
			formService.submitTaskForm(taskId, map);
		} catch (FormFieldValidatorException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return new ResponseEntity<>(e.getMessage() + "Max length: 9", HttpStatus.CONFLICT);
		}
		
		
		NewMagazineFormEditorsReviewersRequestDto revEditorDto = (NewMagazineFormEditorsReviewersRequestDto) runtimeService.getVariable(processInstanceId, "editorReviewersRequest");
		
//		ProcessInstance p =  runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
//		Task tdask = taskService.createTaskQuery().processInstanceId(processInstanceId).list().stream().filter(t -> t.getTaskDefinitionKey().equals("add_editor_and_reviewer")).findFirst().get();
//		revEditorDto.setTaskId(tdask.getId());
//		revEditorDto.setProcessId(processInstanceId);
		
		runtimeService.removeVariable(processInstanceId, "editorReviewersRequest");
		
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
		
        return new ResponseEntity<NewMagazineFormEditorsReviewersRequestDto>(revEditorDto, HttpStatus.OK);
    }
	
	@PostMapping(path = "/submitComplexInfo/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity post(@RequestBody NewMagazineFormEditorsReviewersResponseDto dto, @PathVariable String taskId) {
		
		// Map<String, Object> map = fromDtoToMap(dto);
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		runtimeService.setVariable(processInstanceId, "newMagazineComplexInfo", dto);
		taskService.complete(taskId);
		
		// formService.submitTaskForm(taskId, map);
		
        return new ResponseEntity (HttpStatus.OK);
    }
	
	@GetMapping(path = "/correct/{taskId}", produces = "application/json")
    public @ResponseBody NewMagazineFormRequestDto correctMagazine(@PathVariable String taskId) {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
//		ProcessInstance pi = runtimeService.startProcessInstanceByKey("new_magazine");
//		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskFormData tfd = formService.getTaskFormData(taskId);
		List<FormField> properties = tfd.getFormFields();
		
//		FormField name = properties.stream().filter(f1 -> f1.getId().equals("name")).findFirst().get();
//		FormField issn_number = properties.stream().filter(f2 -> f2.getId().equals("issn_number")).findFirst().get();
//		FormField payment_option = properties.stream().filter(f3 -> f3.getId().equals("payment_option")).findFirst().get();
//		FormField membership_price = properties.stream().filter(f4 -> f4.getId().equals("membership_price")).findFirst().get();
//		FormField science_area_name = properties.stream().filter(f5 -> f5.getId().equals("science_area")).findFirst().get();

		String commentIf = (String) runtimeService.getVariable(task.getProcessInstanceId(), "view_comment");
		commentIf = commentIf != null ? commentIf : "";
		
        return new NewMagazineFormRequestDto(task.getId(), task.getProcessInstanceId(), properties, commentIf);
    }
	
	@PutMapping(path = "/correctBasicInfo/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> corectBasicInfo(@RequestBody NewMagazineFormResponseDto dto, @PathVariable String taskId) {
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		Map<String, Object> map = mapListToDto(dto.getFormFields(), processInstanceId);

		
		NewMagazineFormResponseDto oldMagazineDto = (NewMagazineFormResponseDto) runtimeService.getVariable(processInstanceId, "newMagazineBasicInfo");
		dto.setMagazineDbId(oldMagazineDto.getMagazineDbId());
		dto.setUpdate(true);	
		
		runtimeService.setVariable(processInstanceId, "newMagazineBasicInfo", dto);
		try {
			formService.submitTaskForm(taskId, map);
		} catch (FormFieldValidatorException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return new ResponseEntity<>(e.getMessage() + "Max length: 9", HttpStatus.CONFLICT);
		}
		
		NewMagazineFormEditorsReviewersRequestDto revEditorDto = (NewMagazineFormEditorsReviewersRequestDto) runtimeService.getVariable(processInstanceId, "editorReviewersRequest");		
		// runtimeService.removeVariable(processInstanceId, "editorReviewersRequest");
		
        return new ResponseEntity<NewMagazineFormEditorsReviewersRequestDto>(revEditorDto, HttpStatus.OK);
    }
	
	//@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping(path = "/checkingMagazineData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> checkingMagazineData(@PathVariable String taskId) {
		boolean authorized = authorize(ADMIN_GROUP_ID);
		if(!authorized) {
			return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
		}
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		TaskFormData tfd = formService.getTaskFormData(taskId);
		List<FormField> properties = tfd.getFormFields();
		
		CheckingMagazineDataDto request = (CheckingMagazineDataDto) runtimeService.getVariable(processInstanceId, "checkingMagazineDto");
		// runtimeService.removeVariable(processInstanceId, "checkingMagazineDto");

		request.setFields(properties);
		
        return new ResponseEntity<>(request, HttpStatus.OK);
    }
	
	//@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping(path = "/checkingMagazineData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> checkingMagazineData(@RequestBody CheckingMagazineDataDto dto, @PathVariable String taskId) {
		
		boolean authorized = authorize(ADMIN_GROUP_ID);
		if(!authorized) {
			return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
		}
		
		Map<String, Object> properties = this.mapListToDto(dto.getFieldsResponse(), "");
		formService.submitTaskForm(taskId, properties);
			
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	//neki zesci trip ne uvlaci nista kao da se resutuje kontroler
//	@GetMapping(path = "/alls", produces = "application/json")
//	private @ResponseBody List<MagazineDto> getAllMagazines(){
//    	List<Magazine> magazines = magazineService.getMagazines();
//		
//		
//		List<MagazineDto> magazinesDto = new ArrayList<MagazineDto>();
//		magazines.stream().forEach(m -> {
//			Set<ScienceArea> areas = m.getScienceAreas();
//			List<ScienceAreaDto> areasDto = new ArrayList<ScienceAreaDto>();
//			areas.forEach(a -> areasDto.add(new ScienceAreaDto(a.getScienceAreaId(), a.getScienceAreaName(), a.getScienceAreaCode())));
//			magazinesDto.add(new MagazineDto(m.getMagazineId(), m.getISSN(), m.getName(), areasDto));	
//		});
//		
//		return magazinesDto;
//	}
//	
//	@GetMapping(path = "/{magazineId}", produces = "application/json")
//	private @ResponseBody MagazineDto getMagazine(@PathVariable Long magazineId){
//
//		Magazine magazine = magazineService.getMagazine(magazineId);
//
//		
//		Set<ScienceArea> areas = magazine.getScienceAreas();
//		List<ScienceAreaDto> areasDto = new ArrayList<ScienceAreaDto>();
//		areas.forEach(a -> areasDto.add(new ScienceAreaDto(a.getScienceAreaId(), a.getScienceAreaName(), a.getScienceAreaCode())));
//		
//		return new MagazineDto(magazine.getMagazineId(), magazine.getISSN(), magazine.getName(), areasDto);		
//	}
	
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
		
	private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list, String processInstanceId)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormSubmissionDto temp : list){
			if(!temp.getMultiple().isEmpty()) {
				String value = ((String)temp.getFieldValue()).split(":")[0];
				this.runtimeService.setVariable(processInstanceId, temp.getFieldId() + "Multi", temp.getFieldValue());
				map.put(temp.getFieldId(), value);
			} else {
				map.put(temp.getFieldId(), temp.getFieldValue());
			}
		}
		
		return map;
	}
		
	
} 
