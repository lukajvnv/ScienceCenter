package com.project.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.impl.form.validator.FormFieldValidatorException;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.project.dto.AnalizeDto;
import com.project.dto.ArticleDto;
import com.project.dto.ArticleProcessDto;
import com.project.dto.FormSubmissionDto;
import com.project.dto.MagazineDto;
import com.project.dto.MagazineEditionDto;
import com.project.dto.NewArticleRequestDto;
import com.project.dto.NewArticleResponseDto;
import com.project.dto.NewMagazineFormEditorsReviewersRequestDto;
import com.project.dto.ScienceAreaDto;
import com.project.dto.TermDto;
import com.project.dto.UpdateArticleChangesDto;
import com.project.dto.UpdateArticleDto;
import com.project.dto.UserDto;
import com.project.dto.integration.OrderIdDTO;
import com.project.dto.integration.UserTxDto;
import com.project.model.Article;
import com.project.model.Magazine;
import com.project.model.MagazineEdition;
import com.project.model.ScienceArea;
import com.project.model.Term;
import com.project.model.enums.BuyingType;
import com.project.model.enums.TxStatus;
import com.project.model.user.UserSignedUp;
import com.project.model.user.tx.UserTx;
import com.project.model.user.tx.UserTxItem;
import com.project.repository.UnityOfWork;
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
		
		@Autowired
		private UnityOfWork unityOfWork;
		
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
		
		@RequestMapping(path = "/getEditions/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<List<MagazineEditionDto>> getEditions(@PathVariable Long id) {
			
			Magazine magazine = magazineService.getMagazine(id);

			Set<MagazineEdition> editions = magazine.getMagazineEditions();
			List<MagazineEditionDto> editionsDto = new ArrayList<MagazineEditionDto>();
			
			for(MagazineEdition mE: editions) {
				editionsDto.add(new MagazineEditionDto(mE.getMagazineEditionId(), mE.getPublishingDate(), mE.getMagazineEditionPrice(), mE.getTitle()));
			}
			
			return new ResponseEntity<List<MagazineEditionDto>>(editionsDto, HttpStatus.OK);
		}
		
		@RequestMapping(path = "/getEdition/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<MagazineEditionDto> getEdition(@PathVariable Long id) {
			
			MagazineEdition mE = magazineService.getMagazineEdition(id);

			MagazineEditionDto editionsDto = new MagazineEditionDto(mE.getMagazineEditionId(), mE.getPublishingDate(), mE.getMagazineEditionPrice(), mE.getTitle());
			
			return new ResponseEntity<MagazineEditionDto>(editionsDto, HttpStatus.OK);
		}
		
		@RequestMapping(path = "/newEdition/{magazineId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<MagazineEditionDto> newEdition(@PathVariable Long magazineId, @RequestBody MagazineEditionDto dto) {
			
			Magazine mag = magazineService.getMagazine(magazineId);
			
			MagazineEdition newEdition = MagazineEdition.builder()
											.title(dto.getTitle())
											.magazineEditionPrice(dto.getMagazineEditionPrice())
											.publishingDate(new Date())
											.magazine(mag)
											.build();
			
			MagazineEdition persistedEdition = magazineService.newMagazineEdition(newEdition);
						
			return new ResponseEntity<MagazineEditionDto>(HttpStatus.CREATED);
		}
		
		@RequestMapping(path = "/getArticles/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<List<ArticleDto>> getArticles(@PathVariable Long id) {
			
			MagazineEdition mE = magazineService.getMagazineEdition(id);
			Set<Article> articles = mE.getArticles();

			MagazineEditionDto editionsDto = new MagazineEditionDto(mE.getMagazineEditionId(), mE.getPublishingDate(), mE.getMagazineEditionPrice(), mE.getTitle());
			
			List<ArticleDto> articlesDto = new ArrayList<ArticleDto>();
			
			articles.forEach(a -> {
				ScienceArea sc = a.getScienceArea();
				ScienceAreaDto scDto = new ScienceAreaDto(sc.getScienceAreaId(), sc.getScienceAreaName(), sc.getScienceAreaCode());
				
				UserSignedUp author = a.getAuthor();
				UserDto authorDto = new UserDto(author.getUserId(), author.getFirstName(), author.getLastName(), author.getEmail(), author.getCity(), author.getCountry(), author.getUserUsername(), author.getVocation());
				
				Set<UserSignedUp> coAuthors = a.getCoAuthors();
				List<UserDto> coAuthorsDto = new ArrayList<UserDto>();
				coAuthors.forEach(c -> {
					UserDto coAuthorDto = new UserDto(c.getUserId(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getCity(), c.getCountry(), c.getUserUsername(), c.getVocation());
					coAuthorsDto.add(coAuthorDto);
				});
				
				Set<Term> keyTerms = a.getKeyTerms();
				List<TermDto> keyTermsDto = new ArrayList<TermDto>();
				keyTerms.forEach(k -> {
					keyTermsDto.add(new TermDto(k.getTermId(), k.getTermName()));
				});
				
				ArticleDto dto = new ArticleDto(
						"", 
						"", 
						a.getArticleId(), 
						a.getArticleTitle(), 
						a.getArticleAbstract(), 
						scDto, 
						a.getPublishingDate(), 
						authorDto, 
						coAuthorsDto, 
						keyTermsDto, 
						a.getArticlePrice(), 
						a.getDoi());
				
				articlesDto.add(dto);
			});
			
			return new ResponseEntity<List<ArticleDto>>(articlesDto, HttpStatus.OK);
		}
		
		@RequestMapping(path = "/viewArticle/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseEntity<ArticleDto> viewArticle(@PathVariable Long id) {
			
			Article a = articleService.getArticle(id);
			
			ScienceArea sc = a.getScienceArea();
			ScienceAreaDto scDto = new ScienceAreaDto(sc.getScienceAreaId(), sc.getScienceAreaName(), sc.getScienceAreaCode());
			
			UserSignedUp author = a.getAuthor();
			UserDto authorDto = new UserDto(author.getUserId(), author.getFirstName(), author.getLastName(), author.getEmail(), author.getCity(), author.getCountry(), author.getUserUsername(), author.getVocation());
			
			Set<UserSignedUp> coAuthors = a.getCoAuthors();
			List<UserDto> coAuthorsDto = new ArrayList<UserDto>();
			coAuthors.forEach(c -> {
				UserDto coAuthorDto = new UserDto(c.getUserId(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getCity(), c.getCountry(), c.getUserUsername(), c.getVocation());
				coAuthorsDto.add(coAuthorDto);
			});
			
			Set<Term> keyTerms = a.getKeyTerms();
			List<TermDto> keyTermsDto = new ArrayList<TermDto>();
			keyTerms.forEach(k -> {
				keyTermsDto.add(new TermDto(k.getTermId(), k.getTermName()));
			});
			
			ArticleDto articleDto = new ArticleDto(
					"", 
					"", 
					a.getArticleId(), 
					a.getArticleTitle(), 
					a.getArticleAbstract(), 
					scDto, 
					a.getPublishingDate(), 
					authorDto, 
					coAuthorsDto, 
					keyTermsDto, 
					a.getArticlePrice(), 
					a.getDoi());
						
			return new ResponseEntity<ArticleDto>(articleDto, HttpStatus.OK);
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
		
		@GetMapping(path="/downloadFile/{articleId}", produces = MediaType.APPLICATION_PDF_VALUE) // //new annotation since 4.3
	    public ResponseEntity<?> downloadFileByUser(@PathVariable long articleId) {
			
			String username = "";
			try {
				   username = identityService.getCurrentAuthentication().getUserId(); //ako nema puca exception
				} catch (Exception e) {
					return new ResponseEntity<List<UserTxDto>>(HttpStatus.CONFLICT);
				}
			
			//privremeno 
			UserSignedUp loggedUser = unityOfWork.getUserSignedUpRepository().findByUserUsername(username);
			if(loggedUser == null) {
				return new ResponseEntity<List<UserTxDto>>(HttpStatus.CONFLICT);
			}
			
			List<UserTx> userTxs = loggedUser.getUserTxs().stream().filter(tx -> tx.getStatus() == TxStatus.SUCCESS).collect(Collectors.toList());
			
			List<UserTxItem> filteredItemsArticles = userTxs.stream()
					.flatMap(u -> u.getItems().stream().filter(item -> item.getBuyingType().equals(BuyingType.ARTICLE) && item.getItemId().equals(articleId))).collect(Collectors.toList());
	     
			Article article = articleService.getArticle(articleId);
			MagazineEdition edition = article.getMagazineEdition();
			
			List<UserTxItem> filteredItemsEdition = userTxs.stream()
					.flatMap(u -> u.getItems().stream().filter(item -> item.getBuyingType().equals(BuyingType.MAGAZINE_EDITION) && item.getItemId().equals(edition.getMagazineEditionId()))).collect(Collectors.toList());
			
			//TODO: PRETPLATE...
			
			if(filteredItemsArticles.size() == 0 && filteredItemsEdition.size() == 0) {
				return new ResponseEntity<List<UserTxDto>>(HttpStatus.CONFLICT);
			}
			
			// Get the file and save it somewhere
			byte[] bytes = articleService.getFile(articleId);
			System.out.println(bytes);
			
			String fileName = "employees.pdf";
			
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
		
		@GetMapping(path="/callKp", produces = MediaType.APPLICATION_PDF_VALUE) // //new annotation since 4.3
	    public ResponseEntity<?> callKp() {
	     
			RestTemplate restTemplate = new RestTemplate();
			
//			String url = "https://localhost:8762/requestHandler/request/save";
			String url = "https://localhost:8762/requestHandler/test";

			ResponseEntity<Object> dto = null;
			try {
				dto = restTemplate.exchange(url, HttpMethod.GET, createHeader(null), Object.class);
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return new ResponseEntity<>(HttpStatus.OK);

	    }
		
		@GetMapping(path="/executeAuthorPayment/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE) // //new annotation since 4.3
	    public ResponseEntity<?> callKp(@PathVariable String taskId) {
	     
			boolean authorized = authorize(AUTHOR_GROUP_ID);
			if(!authorized) {
				return new ResponseEntity<>(new Response("Cannot find logged user", HttpStatus.UNAUTHORIZED), HttpStatus.UNAUTHORIZED);
			}
			
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
				
			String processInstanceId = task.getProcessInstanceId();
			OrderIdDTO orderIdDto = (OrderIdDTO) runtimeService.getVariable(processInstanceId, "paymentInfo");
			
			return new ResponseEntity<>(orderIdDto, HttpStatus.OK);

	    }
		
		private HttpEntity<?> createHeader (Object body){
			HttpHeaders headers = new HttpHeaders();
			headers.add("external", "true");
			headers.add("hostsc", "localhost:" + 8085);
			
			HttpEntity<?> entity;
			if(body != null) {
				entity = new HttpEntity<>(body, headers);
			} else {
				entity = new HttpEntity<>(headers);
			}
			
			return entity;
		}
}


