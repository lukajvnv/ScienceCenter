package com.project.controller;

import java.io.IOException;
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
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.AnalizeDto;
import com.project.dto.ArticleDto;
import com.project.dto.ArticleProcessDto;
import com.project.dto.FormSubmissionDto;
import com.project.dto.MagazineDto;
import com.project.dto.NewArticleRequestDto;
import com.project.dto.NewArticleResponseDto;
import com.project.dto.NewMagazineFormEditorsReviewersRequestDto;
import com.project.dto.ScienceAreaDto;
import com.project.dto.UpdateArticleChangesDto;
import com.project.dto.UpdateArticleDto;
import com.project.model.Article;
import com.project.model.Magazine;
import com.project.model.ScienceArea;
import com.project.service.ArticleService;
import com.project.service.MagazineService;
import com.project.util.Response;

@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {

	

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
		private ArticleService articleService;
		
		@Autowired
		MagazineService magazineService;
		
		private final static String ADMIN_GROUP_ID = "camunda-admin";
		private final static String GUEST_GROUP_ID = "guest";
		private final static String REVIEWER_GROUP_ID = "reviewer";
		private final static String EDITOR_GROUP_ID = "editor";
		private final static String AUTHOR_GROUP_ID = "author";
		
		@GetMapping(path = "/startArticleInit/{magazineId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<?> initArticleAdding(@PathVariable Long magazineId) {
			
			boolean authorized = authorize(AUTHOR_GROUP_ID);
			if(!authorized) {
				return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
			}
			
			ProcessInstance pi = runtimeService.startProcessInstanceByKey("new_article");
			Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
			String proccessInstanceId = task.getProcessInstanceId();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("select_magazine_id", magazineId);
			
			formService.submitTaskForm(task.getId(), map);
			
			//da li treba platiti 
			Map<String, Object > vars = null;
			try {
				vars = runtimeService.getVariables(proccessInstanceId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return new ResponseEntity<>(new Response("Error during payment, process finished!!!", HttpStatus.CONFLICT), HttpStatus.CONFLICT);

			}
			Response error_at_payment = (Response) runtimeService.getVariable(proccessInstanceId, "error_at_payment");
			if(error_at_payment != null) {
				return new ResponseEntity<>(new Response("Error during payment, process finished!!!", HttpStatus.CONFLICT), HttpStatus.CONFLICT);
			}
			
			
			boolean shouldPay = (boolean) runtimeService.getVariable(proccessInstanceId, "should_pay");
			
			if(!shouldPay) {
				return new ResponseEntity<>(new Response("Open your task panel to continue adding", HttpStatus.OK), HttpStatus.OK);
			}
			
			
//			if(shouldPay) {
//				return new ResponseEntity<>(new Response("You have to pay first, wait for insturction", HttpStatus.OK), HttpStatus.OK);
//			} else {
//				return new ResponseEntity<>(new Response("Open your task panel to continue adding", HttpStatus.OK), HttpStatus.OK);
//			}
			
			//pukla greska itd...
			
	        return new ResponseEntity<>(new Response("PAID COMPLETED. Open your task panel to continue adding", HttpStatus.OK), HttpStatus.OK);
	    }
		
		@GetMapping(path = "/start/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<?> startAddingArticle(@PathVariable String taskId) {
//			ProcessInstance pi = runtimeService.startProcessInstanceByKey("new_article");
//			Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
//			String proccessInstanceId = task.getProcessInstanceId();
//			
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("select_magazine_id", magazineId);
//			
//			formService.submitTaskForm(task.getId(), map);
//			
//			//da li treba platiti 
//			
//			//pukla greska itd...
			
			boolean authorized = authorize(AUTHOR_GROUP_ID);
			if(!authorized) {
				return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
			}
			
			
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String proccessInstanceId = task.getProcessInstanceId();
			NewArticleRequestDto requestDto = (NewArticleRequestDto) runtimeService.getVariable(proccessInstanceId, "newArticleRequestDto");
			//runtimeService.removeVariable(proccessInstanceId, "newArticleRequestDto");
			
			
			TaskFormData tfd = formService.getTaskFormData(requestDto.getTaskId());
			List<FormField> properties = tfd.getFormFields();
			
			List<FormField> fieldToDisplay = new ArrayList<FormField>(properties);
			FormField field = fieldToDisplay.stream().filter(f -> f.getId().equals("article_file")).findFirst().get();
			fieldToDisplay.remove(field);
			
			requestDto.setFields(fieldToDisplay);
			
	        return new ResponseEntity<NewArticleRequestDto>(requestDto, HttpStatus.OK);
	    }	
		
		@PostMapping(path = "/postArticle/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<?> post(@RequestBody NewArticleResponseDto dto, @PathVariable String taskId) throws IOException {
			
			boolean authorized = authorize(AUTHOR_GROUP_ID);
			if(!authorized) {
				return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
			}
			
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			
//			Decoder decoder = Base64.getDecoder();
//	        byte[] decodedByte = decoder.decode(dto.getFile().split(",")[1]);
//	        FileOutputStream fos = new FileOutputStream("MyAudio.webm");
//	        fos.write(decodedByte);
//	        fos.close();
			
			String processInstanceId = task.getProcessInstanceId();
			// formService.submitTaskForm(taskId, properties);
			
			Map<String, Object> map = mapListToDto(dto.getFields(), processInstanceId);			
			byte[] file = (byte[]) runtimeService.getVariable(processInstanceId, "fileTemp");
			map.put("article_file", file);
			
			runtimeService.setVariable(processInstanceId, "newArticleDto", dto);
			try {
				formService.submitTaskForm(taskId, map);
			} catch (FormFieldValidatorException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return new ResponseEntity<>(e.getMessage() + "Max length: 9", HttpStatus.CONFLICT);
			}

			
	        return new ResponseEntity<NewMagazineFormEditorsReviewersRequestDto>(HttpStatus.OK);
	    }
	
		@GetMapping(path = "/analizeBasic/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<?> get(@PathVariable String taskId) {
			
			boolean authorized = authorize(EDITOR_GROUP_ID);
			if(!authorized) {
				return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
			}

			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String proccessInstanceId = task.getProcessInstanceId();
			
			ArticleDto requestDto = (ArticleDto) runtimeService.getVariable(proccessInstanceId, "articleRequestDto");
			// runtimeService.removeVariable(proccessInstanceId, "articleRequestDto");
			
			TaskFormData tfd = formService.getTaskFormData(taskId);
			List<FormField> properties = tfd.getFormFields();
			
			AnalizeDto analizeDto = new AnalizeDto();
			analizeDto.setDisplayArticle(requestDto);
			analizeDto.setFields(properties);
			
			
	        return new ResponseEntity<AnalizeDto>(analizeDto, HttpStatus.OK);
	    }
		
		@GetMapping(path = "/analizeBasicResult/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<?> get(@PathVariable String taskId, @RequestParam("topicOk") boolean topicOk) {
			boolean authorized = authorize(EDITOR_GROUP_ID);
			if(!authorized) {
				return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
			}
			
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			
			String proccessInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance =  runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();
			
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("is_topic_ok", topicOk);
			formService.submitTaskForm(task.getId(), properties);
			
//			Task taskForAnalizeText = taskService.createTaskQuery().processInstanceId(proccessInstanceId).list().stream().filter(t -> t.getTaskDefinitionKey().equals("Analize_article_text")).findFirst().orElse(null);
//			if( taskForAnalizeText == null ) {
//				
//			}
			
			ProcessInstance processInstance1 =  runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();
			Task taskAnalizeText = null;
			try {
				taskAnalizeText = taskService.createTaskQuery().processInstanceId(proccessInstanceId).list().get(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				
				System.out.println("bacaj ga nekompatabilnost");
	    		//return ResponseEntity.status(error.getStatus()).body(new Response(HttpStatus.));
	    		return new ResponseEntity<>(HttpStatus.OK);

			}

			
			ArticleProcessDto requestDto = (ArticleProcessDto) runtimeService.getVariable(proccessInstanceId, "articleProcessDto");
			String document = articleService.getDocument(requestDto.getArticleId());
			
			ArticleDto articleDto = new ArticleDto();
			articleDto.setTaskId(taskAnalizeText.getId());
			articleDto.setProcessInstanceId(proccessInstanceId);
			articleDto.setFile(document);
			
	        return new ResponseEntity<ArticleDto>(articleDto, HttpStatus.OK);
	    }
		
		@PostMapping(path = "/analizeBasicResult/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<?> postA(@PathVariable String taskId, @RequestBody AnalizeDto request) {
			
			boolean authorized = authorize(EDITOR_GROUP_ID);
			if(!authorized) {
				return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
			}

			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			
			String proccessInstanceId = task.getProcessInstanceId();
			// ProcessInstance processInstance =  runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();
			
			Map<String, Object> map = mapListToDto(request.getFieldResults(), proccessInstanceId);
			try {
				formService.submitTaskForm(taskId, map);
			} catch (FormFieldValidatorException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return new ResponseEntity<>(e.getMessage() + "Max length: 9", HttpStatus.CONFLICT);
			}
			
//			Task taskForAnalizeText = taskService.createTaskQuery().processInstanceId(proccessInstanceId).list().stream().filter(t -> t.getTaskDefinitionKey().equals("Analize_article_text")).findFirst().orElse(null);
//			if( taskForAnalizeText == null ) {
//				
//			}
			
			ProcessInstance processInstance1 =  runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();
			Task taskAnalizeText = null;
			try {
				taskAnalizeText = taskService.createTaskQuery().processInstanceId(proccessInstanceId).list().get(0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				
				System.out.println("bacaj ga nekompatabilnost");
	    		//return ResponseEntity.status(error.getStatus()).body(new Response(HttpStatus.));
				AnalizeDto analizeDto = new AnalizeDto();
				analizeDto.setRedirect(true);
	    		return new ResponseEntity<>(analizeDto, HttpStatus.OK);

			}

			
			ArticleProcessDto requestDto = (ArticleProcessDto) runtimeService.getVariable(proccessInstanceId, "articleProcessDto");
			String document = articleService.getDocument(requestDto.getArticleId());
			
			ArticleDto articleDto = new ArticleDto();
			articleDto.setTaskId(taskAnalizeText.getId());
			articleDto.setProcessInstanceId(proccessInstanceId);
			articleDto.setFile(document);
			
			TaskFormData tfd = formService.getTaskFormData(taskAnalizeText.getId());
			List<FormField> properties = tfd.getFormFields();
			
			AnalizeDto analizeDto = new AnalizeDto();
			analizeDto.setDisplayArticle(articleDto);
			analizeDto.setFields(properties);
			
	        return new ResponseEntity<AnalizeDto>(analizeDto, HttpStatus.OK);
	    }
		
		@PostMapping(path = "/analizeTextResult/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<?> analizeTextResult(@PathVariable String taskId, @RequestBody AnalizeDto req) {
			
			boolean authorized = authorize(EDITOR_GROUP_ID);
			if(!authorized) {
				return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
			}

			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			
			String proccessInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance =  runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();
			
			Map<String, Object> map = mapListToDto(req.getFieldResults(), proccessInstanceId);
			try {
				formService.submitTaskForm(taskId, map);
			} catch (FormFieldValidatorException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return new ResponseEntity<>(e.getMessage() + "Max length: 9", HttpStatus.CONFLICT);
			}
			
			
	        return new ResponseEntity<ArticleDto>(new ArticleDto(), HttpStatus.OK);
	    }
		
		@GetMapping(path = "/updateArticleStart/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<?> updateArticleStart(@PathVariable String taskId) {
			
			boolean authorized = authorize(AUTHOR_GROUP_ID);
			if(!authorized) {
				return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
			}
			
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String proccessInstanceId = task.getProcessInstanceId();
			
						
			UpdateArticleDto requestDto = (UpdateArticleDto) runtimeService.getVariable(proccessInstanceId, "updateArticleRequestDto");
			//runtimeService.removeVariable(proccessInstanceId, "updateArticleRequestDto");
			
	        return new ResponseEntity<UpdateArticleDto>(requestDto, HttpStatus.OK);
	    }	
		
		@PutMapping(path = "/updateArticle/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity updateArticle(@RequestBody NewArticleResponseDto dto, @PathVariable String taskId) throws IOException {
			
			boolean authorized = authorize(AUTHOR_GROUP_ID);
			if(!authorized) {
				return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
			}
			
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			
			String processInstanceId = task.getProcessInstanceId();
			runtimeService.setVariable(processInstanceId, "newArticleDto", dto);
			
			byte[] file = (byte[]) runtimeService.getVariable(processInstanceId, "fileTemp");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("updated_file", file);
			
			try {
				formService.submitTaskForm(taskId, map);
			} catch (FormFieldValidatorException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return new ResponseEntity<>(e.getMessage() + "Max length: 9", HttpStatus.CONFLICT);
			}

			
	        return new ResponseEntity<NewMagazineFormEditorsReviewersRequestDto>(HttpStatus.OK);
	    }
		
		@GetMapping(path = "/updateArticleChangesStart/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<?> updateArticleChangesStart(@PathVariable String taskId) {
			
			boolean authorized = authorize(AUTHOR_GROUP_ID);
			if(!authorized) {
				return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
			}
			
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String proccessInstanceId = task.getProcessInstanceId();
			
						
			UpdateArticleChangesDto requestDto = (UpdateArticleChangesDto) runtimeService.getVariable(proccessInstanceId, "updateArticleChangesDto");
			//runtimeService.removeVariable(proccessInstanceId, "updateArticleChangesDto");
			
			TaskFormData tfd = formService.getTaskFormData(taskId);
			List<FormField> properties = tfd.getFormFields();
			
			List<FormField> fieldToDisplay = new ArrayList<FormField>(properties);
			FormField field = fieldToDisplay.stream().filter(f -> f.getId().equals("change_file")).findFirst().get();
			fieldToDisplay.remove(field);
			
			requestDto.setFields(fieldToDisplay);
			
	        return new ResponseEntity<UpdateArticleChangesDto>(requestDto, HttpStatus.OK);
	    }	
		
		@PutMapping(path = "/updateArticleChangesPut/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity updateArticleChangesPut(@RequestBody UpdateArticleChangesDto dto, @PathVariable String taskId) throws IOException {
			
			boolean authorized = authorize(AUTHOR_GROUP_ID);
			if(!authorized) {
				return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
			}
			
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
				
			String processInstanceId = task.getProcessInstanceId();
			// formService.submitTaskForm(taskId, properties);
			runtimeService.setVariable(processInstanceId, "authorMessage", dto.getAuthorsMessage());
			runtimeService.setVariable(processInstanceId, "newArticleDto", dto.getNewAarticleResponseDto());
			
			Map<String, Object> map = mapListToDto(dto.getFieldResults(), processInstanceId);
			
			byte[] file = (byte[]) runtimeService.getVariable(processInstanceId, "fileTemp");
			map.put("change_file", file);
			
			try {
				formService.submitTaskForm(taskId, map);
			} catch (FormFieldValidatorException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				return new ResponseEntity<>(e.getMessage() + "Max length: 9", HttpStatus.CONFLICT);
			}

			
	        return new ResponseEntity<NewMagazineFormEditorsReviewersRequestDto>(HttpStatus.OK);
	    }
		
		// iz magazines controllera tamo nesto zeza...
		@GetMapping(path = "/magazines", produces = "application/json")
		private @ResponseBody List<MagazineDto> getAllMagazines(){
	    	List<Magazine> magazines = magazineService.getMagazines();
			
			
			List<MagazineDto> magazinesDto = new ArrayList<MagazineDto>();
			magazines.stream().forEach(m -> {
				Set<ScienceArea> areas = m.getScienceAreas();
				List<ScienceAreaDto> areasDto = new ArrayList<ScienceAreaDto>();
				areas.forEach(a -> areasDto.add(new ScienceAreaDto(a.getScienceAreaId(), a.getScienceAreaName(), a.getScienceAreaCode())));
				magazinesDto.add(new MagazineDto(m.getMagazineId(), m.getISSN(), m.getName(), m.getWayOfPayment(), areasDto));	
			});
			
			return magazinesDto;
		}
		
		@GetMapping(path = "/magazine/{magazineId}", produces = "application/json")
		private @ResponseBody MagazineDto getMagazine(@PathVariable Long magazineId){

			Magazine magazine = magazineService.getMagazine(magazineId);

			
			Set<ScienceArea> areas = magazine.getScienceAreas();
			List<ScienceAreaDto> areasDto = new ArrayList<ScienceAreaDto>();
			areas.forEach(a -> areasDto.add(new ScienceAreaDto(a.getScienceAreaId(), a.getScienceAreaName(), a.getScienceAreaCode())));
			
			return new MagazineDto(magazine.getMagazineId(), magazine.getISSN(), magazine.getName(), magazine.getWayOfPayment(), areasDto);		
		}
		
		@PostMapping("/upload/{taskId}") // //new annotation since 4.3
	    public ResponseEntity<?> singleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable String taskId) {
	        try {
	        	Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
				
				String processInstanceId = task.getProcessInstanceId();
				runtimeService.setVariable(processInstanceId, "fileTemp", file.getBytes());
				
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        return new ResponseEntity<>(HttpStatus.OK);
	    }
		
		
		@GetMapping(path="/download/{articleId}", produces = MediaType.APPLICATION_PDF_VALUE) // //new annotation since 4.3
	    public ResponseEntity<?> download(@PathVariable long articleId) {
	     
			// Get the file and save it somewhere
			byte[] bytes = articleService.getFile(articleId);
			System.out.println(bytes);
			
			String fileName = "employees.pdf";
//			HttpHeaders respHeaders = new HttpHeaders();
//			respHeaders.setContentLength(bytes.length);
//			respHeaders.setContentType(new MediaType("application", "pdf"));
//			respHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//			respHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
////			return new ResponseEntity<byte[]>(bytes, respHeaders, HttpStatus.OK);
//			return new ResponseEntity<>(bytes, respHeaders, HttpStatus.OK);
			
			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_PDF)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
					.body(bytes);

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
		
		private HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list, String processInstanceId)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			for(FormSubmissionDto temp : list){
				if(!temp.getMultiple().isEmpty()) {
					String value = ((String)temp.getFieldValue()).split(":")[0];
					this.runtimeService.setVariable(processInstanceId, temp.getFieldId() + "Multi", temp.getFieldValue());
					map.put(temp.getFieldId(), value);
				} else if(!temp.getMultiEnum().isEmpty()) {
					map.put(temp.getFieldId(), temp.getFieldMultiValues());
				}
				
				else {
					map.put(temp.getFieldId(), temp.getFieldValue());
				}
			}
			
			return map;
		}
}


