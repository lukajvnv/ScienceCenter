package com.project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.VariableInstance;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.AddReviewersDto;
import com.project.dto.ArticleDto;
import com.project.dto.ArticleProcessDto;
import com.project.dto.EditorReviewerByScienceAreaDto;
import com.project.dto.FormSubmissionDto;
import com.project.dto.MagazineDto;
import com.project.dto.ReviewArticleMfDto;
import com.project.dto.ReviewerFilterDto;
import com.project.dto.ReviewingDto;
import com.project.dto.ReviewingEditorDto;
import com.project.dto.ScienceAreaDto;
import com.project.dto.TermDto;
import com.project.dto.UserDto;
import com.project.model.Article;
import com.project.model.Magazine;
import com.project.model.OpinionAboutArticle;
import com.project.model.ScienceArea;
import com.project.model.Term;
import com.project.model.enums.ArticleStatus;
import com.project.model.enums.ReviewingType;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;
import com.project.service.ArticleService;
import com.project.service.MagazineService;

@RestController
@RequestMapping("/review")
@CrossOrigin
public class ReviewingController {

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
	
	@Autowired
	private UnityOfWork unityOfWork;
	
	@Autowired
	private ArticleService articleService;
	
	@GetMapping(path = "/addReviewer/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<AddReviewersDto> addReviewer(@PathVariable String taskId) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String proccessInstanceId = task.getProcessInstanceId();
		
		AddReviewersDto requestDto = (AddReviewersDto) runtimeService.getVariable(proccessInstanceId, "addReviewersDto");
		// runtimeService.removeVariable(proccessInstanceId, "addReviewersDto");
		
		TaskFormData data = formService.getTaskFormData(taskId);
		List<FormField> fields =  data.getFormFields();
		requestDto.setFields(fields);
		
        return new ResponseEntity<AddReviewersDto>(requestDto, HttpStatus.OK);
    }
		
	@PostMapping(path = "/addReviewer/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> addReviewer(@PathVariable String taskId, @RequestBody List<FormSubmissionDto> response) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		String proccessInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance =  runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();
				
		Map<String, Object> map = mapListToDto(response, proccessInstanceId);
		try {
			formService.submitTaskForm(taskId, map);
		} catch (FormFieldValidatorException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		

		
        return new ResponseEntity<ArticleDto>(HttpStatus.OK);
    }
	
	@PostMapping(path = "/filterReviewer/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> filterReviewer(@PathVariable String taskId, @RequestBody ReviewerFilterDto searchRequest) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		String proccessInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance =  runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();
		
		List<EditorReviewerByScienceAreaDto> reviewersDto = (ArrayList<EditorReviewerByScienceAreaDto>) runtimeService.getVariable(proccessInstanceId, "reviewersByScArea");
		List<EditorReviewerByScienceAreaDto> reviewersDtoFilter = new ArrayList<EditorReviewerByScienceAreaDto>(reviewersDto);
		
		ArticleProcessDto articleProcessDto = (ArticleProcessDto)  runtimeService.getVariable(proccessInstanceId, "articleProcessDto");

		
		if(searchRequest.isScienceAreaFilter()) {
			Article article = unityOfWork.getArticleRepository().getOne(articleProcessDto.getArticleId());
			ScienceArea scArea = article.getScienceArea();
			
			reviewersDtoFilter = reviewersDto.stream()
				.filter(r -> r.getScienceArea().getScienceAreaId().equals(scArea.getScienceAreaId()))
				.collect(Collectors.toList());
			
		}
		
		List<EditorReviewerByScienceAreaDto> revToRemove = new ArrayList<EditorReviewerByScienceAreaDto>();

		
		if(searchRequest.isGeoFilter()) {
			UserSignedUp author = unityOfWork.getUserSignedUpRepository().findByUserUsername(articleProcessDto.getAuthor());
			
			if(author.getLatitude() != null && author.getLatitude() != null) {
				reviewersDtoFilter.forEach(r -> {	
					if(r.getLatitude() != null && r.getLatitude() != null) {
						double dist = getDistanceFromLatLonInKm(author.getLatitude(), author.getLongitude(), r.getLatitude(), r.getLongitude());
						if(dist < 100) {
							revToRemove.add(r);
						}
					}
						
				});
			}

			
		}
		
		reviewersDtoFilter.removeAll(revToRemove);
		
		// reviewers enum datasource init
		TaskFormData formData =  formService.getTaskFormData(taskId);
		List<FormField> fields = formData.getFormFields();
		FormField field = formData.getFormFields().stream().filter(f -> f.getId().equals("reviewer_begin")).findFirst().get();
		EnumFormType ft = (EnumFormType)field.getType();
		Map<String, String> map = ft.getValues();
		map.clear();
		
		reviewersDtoFilter.forEach(rev -> { 
			ScienceAreaDto scDto = rev.getScienceArea();
			UserDto userDto = rev.getEditorReviewer();
			String text = new StringBuilder(userDto.getFirstName())
					.append(" ")
					.append(userDto.getLastName())
					.append(", ")
					.append(userDto.getCity())
					.append(", scienceArea: ")
					.append(scDto.getScienceAreaCode())
					.append(" : ")
					.append(scDto.getScienceAreaName())
					.toString();
		  map.put(rev.getEditorByScArId().toString(), text);
		});

		
        return new ResponseEntity<>(fields, HttpStatus.OK);
    }
	
	@GetMapping(path = "/addReviewerWhenError/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<AddReviewersDto> addReviewerWhenError(@PathVariable String taskId) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String proccessInstanceId = task.getProcessInstanceId();
		
		AddReviewersDto requestDto = (AddReviewersDto) runtimeService.getVariable(proccessInstanceId, "addReviewersDto");
		// runtimeService.removeVariable(proccessInstanceId, "addReviewersDto");
		
		TaskFormData data = formService.getTaskFormData(taskId);
		List<FormField> fields =  data.getFormFields();
		requestDto.setFields(fields);
		
        return new ResponseEntity<AddReviewersDto>(requestDto, HttpStatus.OK);
    }
	
	@PostMapping(path = "/addReviewerWhenError/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> addReviewerWhenErrorPost(@PathVariable String taskId, @RequestBody AddReviewersDto response) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		String proccessInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance =  runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();
		
		
		
		VariableInstance reviewVariableMf = runtimeService.createVariableInstanceQuery()
                .processInstanceIdIn(proccessInstanceId)
                .variableName("subProcessData")
                .list()
                .stream()
                .filter(v -> ((ReviewArticleMfDto) v.getValue()).getSubProcessExecutionId().equals(response.getSubProcessMfExecutionId())).findFirst().orElse(null);
		
		List<VariableInstance> reviewVariablseMf = runtimeService.createVariableInstanceQuery()
                .processInstanceIdIn(proccessInstanceId)
                .variableName("subProcessData")
                .list();
		
		String subProcessExecutionId = reviewVariableMf.getExecutionId();
		
		ReviewArticleMfDto reviewArticleMfDto = (ReviewArticleMfDto) reviewVariableMf.getValue();
		
		Map<String, Object> map = mapListToDto(response.getFieldValues(), proccessInstanceId);
		String newTaskInitiator = (String) map.get("reviewer_error");
		
//		reviewArticleMfDto.setTaskInitiator(response.getEditorsReviewersDto().get(0).getEditorReviewer().getUserUsername());
		reviewArticleMfDto.setTaskInitiator(newTaskInitiator);

		runtimeService.setVariable(subProcessExecutionId, "subProcessData", reviewArticleMfDto);
		
		try {
			formService.submitTaskForm(taskId, map);
		} catch (FormFieldValidatorException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		
        return new ResponseEntity<ArticleDto>(new ArticleDto(), HttpStatus.OK);
    }
	
//	@GetMapping(path = "/addAdditionalReviewer/{taskId}", produces = "application/json")
//    public @ResponseBody ResponseEntity<AddReviewersDto> addAdditionalReviewer(@PathVariable String taskId) {
//
//		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//		String proccessInstanceId = task.getProcessInstanceId();
//		
//		AddReviewersDto requestDto = (AddReviewersDto) runtimeService.getVariable(proccessInstanceId, "addReviewersDto");
//		// runtimeService.removeVariable(proccessInstanceId, "addReviewersDto");
//		
//        return new ResponseEntity<AddReviewersDto>(requestDto, HttpStatus.OK);
//    }
	
	@PostMapping(path = "/addAdditionalReviewer/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> addAdditionalReviewerPost(@PathVariable String taskId, @RequestBody AddReviewersDto response) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String proccessInstanceId = task.getProcessInstanceId();
		
		runtimeService.setVariable(proccessInstanceId, "addReviewersDto", response);
		// runtimeService.removeVariable(proccessInstanceId, "addReviewersDto");
		
		Map<String, Object> map = mapListToDto(response.getFieldValues(), proccessInstanceId);
		try {
			formService.submitTaskForm(taskId, map);
		} catch (FormFieldValidatorException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
        return new ResponseEntity<ArticleDto>(new ArticleDto(), HttpStatus.OK);
    }
	
	@GetMapping(path = "/startReviewingAdditional/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity<ReviewingDto> reviewAdditional (@PathVariable String taskId){
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String proccessInstanceId = task.getProcessInstanceId();
		
		ReviewingDto reviewingDto = (ReviewingDto) runtimeService.getVariable(proccessInstanceId, "additionalReviewing");
		// runtimeService.removeVariable(proccessInstanceId, "addReviewersDto");

		TaskFormData data = formService.getTaskFormData(taskId);
		List<FormField> fields =  data.getFormFields();
		reviewingDto.setFields(fields);
		
		return new ResponseEntity<ReviewingDto>(reviewingDto, HttpStatus.OK);
	}
	
	@PostMapping(path = "/startReviewingAdditional/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity<?> reviewAdditionalPost (@PathVariable String taskId, @RequestBody ReviewingDto requestBody){
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String proccessInstanceId = task.getProcessInstanceId();

		
		runtimeService.setVariable(proccessInstanceId, "additionalReviewing", requestBody);
		
		Map<String, Object> map = mapListToDto(requestBody.getFieldResults(), proccessInstanceId);
		try {
			formService.submitTaskForm(taskId, map);
		} catch (FormFieldValidatorException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		
		return new ResponseEntity<ReviewingDto>(new ReviewingDto(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/editorReview/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<ReviewingEditorDto> editorReview(@PathVariable String taskId) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String proccessInstanceId = task.getProcessInstanceId();
		
		ReviewingEditorDto requestDto = (ReviewingEditorDto) runtimeService.getVariable(proccessInstanceId, "editorsReviewing");
		// runtimeService.removeVariable(proccessInstanceId, "addReviewersDto");
		
		TaskFormData data = formService.getTaskFormData(taskId);
		List<FormField> fields =  data.getFormFields();
		// requestDto.setFields(fields);
		
		requestDto.setFields(fields);
		
        return new ResponseEntity<ReviewingEditorDto>(requestDto, HttpStatus.OK);
    }
	
	@PostMapping(path = "/editorReview/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> editorReviewPost(@PathVariable String taskId, @RequestBody ReviewingEditorDto response) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		String proccessInstanceId = task.getProcessInstanceId();
		ProcessInstance processInstance =  runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();
		
		runtimeService.setVariable(proccessInstanceId, "editorsReviewing", response);
		
		Map<String, Object> map = mapListToDto(response.getFieldResults(), proccessInstanceId);
		
		try {
			formService.submitTaskForm(taskId, map);
		} catch (FormFieldValidatorException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}


		
        return new ResponseEntity<ReviewingEditorDto>(new ReviewingEditorDto(), HttpStatus.OK);
    }
	
	@GetMapping(path = "/defineTimeForReview/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> defineTimeForReviewStart(@PathVariable String taskId) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String proccessInstanceId = task.getProcessInstanceId();
		
		TaskFormData data = formService.getTaskFormData(taskId);
		List<FormField> fields =  data.getFormFields();
		
		
        return new ResponseEntity<>(fields, HttpStatus.OK);
    }
	

	@PostMapping(path = "/defineTimeForReview/{taskId}", produces = "application/json")
    public @ResponseBody ResponseEntity<?> defineTimeForReviewPost(@PathVariable String taskId, @RequestBody List<FormSubmissionDto> fields) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String proccessInstanceId = task.getProcessInstanceId();
		
		Map<String, Object> map = mapListToDto(fields, proccessInstanceId);
		try {
			formService.submitTaskForm(taskId, map);
		} catch (FormFieldValidatorException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@GetMapping(path = "/start/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity<ReviewingDto> review (@PathVariable String taskId){
		
		ReviewingDto dto = createReviewingDto(taskId);
		
		TaskFormData data = formService.getTaskFormData(taskId);
		List<FormField> fields =  data.getFormFields();
		// requestDto.setFields(fields);
		
		dto.setFields(fields);
		
		return new ResponseEntity<ReviewingDto>(dto, HttpStatus.OK);
	}
	
	@PostMapping(path = "/reviewPost/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity<?> reviewPost (@PathVariable String taskId, @RequestBody ReviewingDto requestBody){
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String proccessInstanceId = task.getProcessInstanceId();
		
		Map<String, Object> map = mapListToDto(requestBody.getFieldResults(), proccessInstanceId);
		
		String rev_comment = (String) map.get("rev_comment");
		String rev_comment_editor = (String) map.get("rev_comment_editor");
		String rev_decision = (String) map.get("rev_decision");
//
		requestBody.getOpinion().setComment(rev_comment);
		requestBody.getOpinion().setCommentOnlyForEditor(rev_comment_editor);
		requestBody.getOpinion().setOpinion(ArticleStatus.valueOf(rev_decision));

		VariableInstance reviewVariableMf = runtimeService.createVariableInstanceQuery()
                .processInstanceIdIn(proccessInstanceId)
                .variableName("subProcessData")
                .list()
                .stream()
                .filter(v -> ((ReviewArticleMfDto) v.getValue()).getTaskInitiator().equals(task.getAssignee())).findFirst().orElse(null);
		
		if(reviewVariableMf == null) {}
		
		ReviewArticleMfDto before = (ReviewArticleMfDto) reviewVariableMf.getValue();
		
		String subProcessExecutionId = reviewVariableMf.getExecutionId();
		
		ReviewArticleMfDto reviewArticleMfDto = (ReviewArticleMfDto) reviewVariableMf.getValue();
		reviewArticleMfDto.setReviewerOpinion(requestBody);
		
		runtimeService.setVariable(subProcessExecutionId, "subProcessData", reviewArticleMfDto);
		
		try {
			formService.submitTaskForm(taskId, map);
		} catch (FormFieldValidatorException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<ReviewingDto>(new ReviewingDto(), HttpStatus.OK);
	}
	
	private ReviewingDto createReviewingDto(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		
		String proccessInstanceId = task.getProcessInstanceId();
		
		ArticleProcessDto articleProcessDto = (ArticleProcessDto) runtimeService.getVariable(proccessInstanceId, "articleProcessDto");

		Article article = unityOfWork.getArticleRepository().getOne(articleProcessDto.getArticleId());
		Magazine magazine = unityOfWork.getMagazineRepository().getOne(articleProcessDto.getMagazineId());
		UserSignedUp author = unityOfWork.getUserSignedUpRepository().findByUserUsername(articleProcessDto.getAuthor());
		Set<UserSignedUp> coAuthors = article.getCoAuthors();
		List<UserDto> coAuthorsDto = new ArrayList<UserDto>();
		
		coAuthors.stream().forEach(c -> {
			coAuthorsDto.add(new UserDto(c.getUserId(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getCity(), c.getCountry(), c.getUserUsername(), c.getVocation()));
		});
		
		ScienceArea scienceArea = article.getScienceArea();
		ScienceAreaDto scienceAreaDto = new ScienceAreaDto(scienceArea.getScienceAreaId(), scienceArea.getScienceAreaName(), scienceArea.getScienceAreaCode());
		
		UserDto authorDto = new UserDto(author.getUserId(), author.getFirstName(), author.getLastName(), author.getEmail(), author.getCity(), author.getCountry(), author.getUserUsername(), author.getVocation());
		
		Set<Term> terms = article.getKeyTerms();		
		List<TermDto> termsDto = new ArrayList<TermDto>();
		terms.stream().forEach(t -> {
			termsDto.add( new TermDto(t.getTermId(), t.getTermName()));
		});
		
		String document = articleService.getDocument(article.getArticleId());

		ArticleDto articleDto = new ArticleDto(task.getId(), proccessInstanceId, article.getArticleId(), article.getArticleTitle(), article.getArticleAbstract(), 
				scienceAreaDto, article.getPublishingDate(), authorDto, coAuthorsDto, termsDto, article.getArticlePrice(), document);
		
		Set<ScienceArea> scienceAreas = magazine.getScienceAreas();
		List<ScienceAreaDto> scienceAreasDto = new ArrayList<ScienceAreaDto>();
		scienceAreas.stream().forEach(sc -> {
			scienceAreasDto.add(new ScienceAreaDto(sc.getScienceAreaId(), sc.getScienceAreaName(), sc.getScienceAreaCode()));
		});
		
		MagazineDto magazineDto = new MagazineDto(magazine.getMagazineId(), magazine.getISSN(), magazine.getName(), scienceAreasDto);
		
		VariableInstance user = runtimeService.createVariableInstanceQuery()
                .processInstanceIdIn(proccessInstanceId)
                .variableName("subProcessData")
                .list()
                .stream()
                .filter(v -> ((ReviewArticleMfDto) v.getValue()).getTaskInitiator().equals(task.getAssignee())).findFirst().orElse(null);
		
		if(user == null) {}
		
		String personOpinionId = ((ReviewArticleMfDto)user.getValue()).getTaskInitiator();
		
		OpinionAboutArticle opinion = new OpinionAboutArticle(article.getArticleId(), personOpinionId, ReviewingType.REVIEWING, ArticleStatus.ACCEPTED, "", "", articleProcessDto.getIteration());
		
		ReviewingDto reviewingDto = new ReviewingDto(articleDto, magazineDto, opinion, articleProcessDto.getAuthorsMessages(), true, null, null);
		
		
//		VariableInstance user = runtimeService.createVariableInstanceQuery()
//                .processInstanceIdIn(proccessInstanceId)
//                .variableName("task_initiator")
//                .list()
//                .stream()
//                .filter(v -> v.getValue().toString().equals(task.getAssignee())).findFirst().orElse(null);
//		String userId = user.getValue().toString();
//		
//		Set<String> subProcessExecutionIds = runtimeService.createVariableInstanceQuery()
//                .processInstanceIdIn(proccessInstanceId)
//                .variableName("task_initiator")
//                .list()
//                .stream()
//                .map(VariableInstance::getExecutionId)
//                .collect(Collectors.toSet());
		
		
		return reviewingDto;
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
	
	public double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
	    long R = 6371; // Radius of the earth in km
	    double dLat = Math.toRadians(lat2-lat1);  // deg2rad below
	    double dLon = Math.toRadians(lon2-lon1); 
	    double a = 
	      Math.sin(dLat/2) * Math.sin(dLat/2) +
	      Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
	      Math.sin(dLon/2) * Math.sin(dLon/2); 
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
	    double d = R * c; // Distance in km
	    return d;
	}
	
}