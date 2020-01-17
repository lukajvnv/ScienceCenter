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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.CheckingMagazineDataDto;
import com.project.dto.FormFieldsDto;
import com.project.dto.FormSubmissionDto;
import com.project.dto.MagazineDto;
import com.project.dto.NewMagazineFormEditorsReviewersRequestDto;
import com.project.dto.NewMagazineFormEditorsReviewersResponseDto;
import com.project.dto.NewMagazineFormRequestDto;
import com.project.dto.NewMagazineFormResponseDto;
import com.project.dto.ScienceAreaDto;
import com.project.model.Magazine;
import com.project.model.ScienceArea;
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
	private MagazineService magazineService;
	
	@GetMapping(path = "/start", produces = "application/json")
    public @ResponseBody NewMagazineFormRequestDto get() {
		//provera da li korisnik sa id-jem pera postoji
		//List<User> users = identityService.createUserQuery().userId("pera").list();
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("new_magazine");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		
		FormField name = properties.stream().filter(f1 -> f1.getId().equals("name")).findFirst().get();
		FormField issn_number = properties.stream().filter(f2 -> f2.getId().equals("issn_number")).findFirst().get();
		FormField payment_option = properties.stream().filter(f3 -> f3.getId().equals("payment_option")).findFirst().get();
		FormField membership_price = properties.stream().filter(f4 -> f4.getId().equals("membership_price")).findFirst().get();
		FormField science_area_name = properties.stream().filter(f5 -> f5.getId().equals("science_area")).findFirst().get();

	
		
        return new NewMagazineFormRequestDto(task.getId(), pi.getId(), name, issn_number, payment_option, membership_price, science_area_name, "");
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
		
		Map<String, Object> map = fromDtoToMap(dto);
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		runtimeService.setVariable(processInstanceId, "newMagazineBasicInfo", dto);
		formService.submitTaskForm(taskId, map);
		
		
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
		
		FormField name = properties.stream().filter(f1 -> f1.getId().equals("name")).findFirst().get();
		FormField issn_number = properties.stream().filter(f2 -> f2.getId().equals("issn_number")).findFirst().get();
		FormField payment_option = properties.stream().filter(f3 -> f3.getId().equals("payment_option")).findFirst().get();
		FormField membership_price = properties.stream().filter(f4 -> f4.getId().equals("membership_price")).findFirst().get();
		FormField science_area_name = properties.stream().filter(f5 -> f5.getId().equals("science_area")).findFirst().get();

		String commentIf = (String) runtimeService.getVariable(task.getProcessInstanceId(), "view_comment");
		commentIf = commentIf != null ? commentIf : "";
		
        return new NewMagazineFormRequestDto(task.getId(), task.getProcessInstanceId(), name, issn_number, payment_option, membership_price, science_area_name, commentIf);
    }
	
	@PutMapping(path = "/correctBasicInfo/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<NewMagazineFormEditorsReviewersRequestDto> corectBasicInfo(@RequestBody NewMagazineFormResponseDto dto, @PathVariable String taskId) {
		
		Map<String, Object> map = fromDtoToMap(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		NewMagazineFormResponseDto oldMagazineDto = (NewMagazineFormResponseDto) runtimeService.getVariable(processInstanceId, "newMagazineBasicInfo");
		dto.setMagazineDbId(oldMagazineDto.getMagazineDbId());
		dto.setUpdate(true);	
		
		runtimeService.setVariable(processInstanceId, "newMagazineBasicInfo", dto);
		formService.submitTaskForm(taskId, map);
		
		
		NewMagazineFormEditorsReviewersRequestDto revEditorDto = (NewMagazineFormEditorsReviewersRequestDto) runtimeService.getVariable(processInstanceId, "editorReviewersRequest");
				
		runtimeService.removeVariable(processInstanceId, "editorReviewersRequest");
		
        return new ResponseEntity<NewMagazineFormEditorsReviewersRequestDto>(revEditorDto, HttpStatus.OK);
    }
	
	@GetMapping(path = "/checkingMagazineData/{taskId}", produces = "application/json")
    public @ResponseBody CheckingMagazineDataDto checkingMagazineData(@PathVariable String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		
		CheckingMagazineDataDto request = (CheckingMagazineDataDto) runtimeService.getVariable(processInstanceId, "checkingMagazineDto");
		// runtimeService.removeVariable(processInstanceId, "checkingMagazineDto");

        return request;
    }
	
	@PostMapping(path = "/checkingMagazineData/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> checkingMagazineData(@RequestBody CheckingMagazineDataDto dto, @PathVariable String taskId) {
		
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("view_comment", dto.getComment());
		properties.put("view_is_valid", dto.isValid());
		formService.submitTaskForm(taskId, properties);
			
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@GetMapping(path = "/all", produces = "application/json")
	private @ResponseBody List<MagazineDto> getAllMagazines(){
		List<Magazine> magazines = magazineService.getMagazines();
		
		List<MagazineDto> magazinesDto = new ArrayList<MagazineDto>();
		magazines.stream().forEach(m -> {
			Set<ScienceArea> areas = m.getScienceAreas();
			List<ScienceAreaDto> areasDto = new ArrayList<ScienceAreaDto>();
			areas.forEach(a -> areasDto.add(new ScienceAreaDto(a.getScienceAreaId(), a.getScienceAreaName(), a.getScienceAreaCode())));
			magazinesDto.add(new MagazineDto(m.getMagazineId(), m.getISSN(), m.getName(), areasDto));	
		});
		
		return magazinesDto;
	}
	
	@GetMapping(path = "/{magazineId}", produces = "application/json")
	private @ResponseBody MagazineDto getMagazine(@PathVariable Long magazineId){
		Magazine magazine = magazineService.getMagazine(magazineId);
		
		Set<ScienceArea> areas = magazine.getScienceAreas();
		List<ScienceAreaDto> areasDto = new ArrayList<ScienceAreaDto>();
		areas.forEach(a -> areasDto.add(new ScienceAreaDto(a.getScienceAreaId(), a.getScienceAreaName(), a.getScienceAreaCode())));
		
		return new MagazineDto(magazine.getMagazineId(), magazine.getISSN(), magazine.getName(), areasDto);		
	}
		
	private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormSubmissionDto temp : list){
			map.put(temp.getFieldId(), temp.getFieldValue());
		}
		
		return map;
	}
	
	private HashMap<String, Object> fromDtoToMap(NewMagazineFormResponseDto dto){
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", dto.getName());
		map.put("issn_number", dto.getIssn_number());
		map.put("membership_price", dto.getMembership_price());
//		map.put(key, remappingFunction);
//		map.put(key, remappingFunction);

		return map;
	}
} 
