package com.project.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.ArticleDto;
import com.project.dto.ArticleProcessDto;
import com.project.dto.NewArticleRequestDto;
import com.project.dto.NewArticleResponseDto;
import com.project.dto.NewMagazineFormEditorsReviewersRequestDto;
import com.project.dto.UpdateArticleChangesDto;
import com.project.dto.UpdateArticleDto;
import com.project.service.ArticleService;

@RestController
@RequestMapping("/article")
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
		
		@GetMapping(path = "/start/{magazineId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<NewArticleRequestDto> get(@PathVariable Long magazineId) {
			ProcessInstance pi = runtimeService.startProcessInstanceByKey("new_article");
			Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
			String proccessInstanceId = task.getProcessInstanceId();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("select_magazine_id", magazineId);
			
			formService.submitTaskForm(task.getId(), map);
			
			NewArticleRequestDto requestDto = (NewArticleRequestDto) runtimeService.getVariable(proccessInstanceId, "newArticleRequestDto");
			runtimeService.removeVariable(proccessInstanceId, "newArticleRequestDto");
			
	        return new ResponseEntity<NewArticleRequestDto>(requestDto, HttpStatus.OK);
	    }	
		
		@PostMapping(path = "/postArticle/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity post(@RequestBody NewArticleResponseDto dto, @PathVariable String taskId) throws IOException {
			
			
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			
//			Decoder decoder = Base64.getDecoder();
//	        byte[] decodedByte = decoder.decode(dto.getFile().split(",")[1]);
//	        FileOutputStream fos = new FileOutputStream("MyAudio.webm");
//	        fos.write(decodedByte);
//	        fos.close();
			
			String processInstanceId = task.getProcessInstanceId();
			// formService.submitTaskForm(taskId, properties);
			runtimeService.setVariable(processInstanceId, "newArticleDto", dto);
			formService.submitTaskForm(taskId, new HashMap<String, Object>());
//			taskService.complete(taskId);

			
	        return new ResponseEntity<NewMagazineFormEditorsReviewersRequestDto>(HttpStatus.OK);
	    }
	
		@GetMapping(path = "/analizeBasic/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<ArticleDto> get(@PathVariable String taskId) {

			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String proccessInstanceId = task.getProcessInstanceId();
			
			ArticleDto requestDto = (ArticleDto) runtimeService.getVariable(proccessInstanceId, "articleRequestDto");
			runtimeService.removeVariable(proccessInstanceId, "articleRequestDto");
			
	        return new ResponseEntity<ArticleDto>(requestDto, HttpStatus.OK);
	    }
		
		@GetMapping(path = "/analizeBasicResult/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<ArticleDto> get(@PathVariable String taskId, @RequestParam("topicOk") boolean topicOk) {

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
			}

			
			ArticleProcessDto requestDto = (ArticleProcessDto) runtimeService.getVariable(proccessInstanceId, "articleProcessDto");
			String document = articleService.getDocument(requestDto.getArticleId());
			
			ArticleDto articleDto = new ArticleDto();
			articleDto.setTaskId(taskAnalizeText.getId());
			articleDto.setProcessInstanceId(proccessInstanceId);
			articleDto.setFile(document);
			
	        return new ResponseEntity<ArticleDto>(articleDto, HttpStatus.OK);
	    }
		
		@PostMapping(path = "/analizeTextResult/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<ArticleDto> analizeTextResult(@PathVariable String taskId, @RequestParam("textOk") boolean textOk, @RequestBody String comment) {

			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			
			String proccessInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance =  runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();
			
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("is_text_ok", textOk);
			properties.put("analize_article_comment", comment);
			formService.submitTaskForm(task.getId(), properties);
			
////			Task taskForAnalizeText = taskService.createTaskQuery().processInstanceId(proccessInstanceId).list().stream().filter(t -> t.getTaskDefinitionKey().equals("Analize_article_text")).findFirst().orElse(null);
////			if( taskForAnalizeText == null ) {
////				
////			}
//			
//			ProcessInstance processInstance1 =  runtimeService.createProcessInstanceQuery().processInstanceId(proccessInstanceId).singleResult();
//			Task taskAnalizeText = null;
//			try {
//				taskAnalizeText = taskService.createTaskQuery().processInstanceId(proccessInstanceId).list().get(0);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//			
//			ArticleProcessDto requestDto = (ArticleProcessDto) runtimeService.getVariable(proccessInstanceId, "articleProcessDto");
//			String document = articleService.getDocument(requestDto.getArticleId());
//			
//			ArticleDto articleDto = new ArticleDto();
//			articleDto.setTaskId(taskAnalizeText.getId());
//			articleDto.setProcessInstanceId(proccessInstanceId);
//			articleDto.setFile(document);
			
	        return new ResponseEntity<ArticleDto>(new ArticleDto(), HttpStatus.OK);
	    }
		
		@GetMapping(path = "/updateArticleStart/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<UpdateArticleDto> updateArticleStart(@PathVariable String taskId) {
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String proccessInstanceId = task.getProcessInstanceId();
			
						
			UpdateArticleDto requestDto = (UpdateArticleDto) runtimeService.getVariable(proccessInstanceId, "updateArticleRequestDto");
			runtimeService.removeVariable(proccessInstanceId, "updateArticleRequestDto");
			
	        return new ResponseEntity<UpdateArticleDto>(requestDto, HttpStatus.OK);
	    }	
		
		@PutMapping(path = "/updateArticle/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity updateArticle(@RequestBody NewArticleResponseDto dto, @PathVariable String taskId) throws IOException {
			
			
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			
//			Decoder decoder = Base64.getDecoder();
//	        byte[] decodedByte = decoder.decode(dto.getFile().split(",")[1]);
//	        FileOutputStream fos = new FileOutputStream("MyAudio.webm");
//	        fos.write(decodedByte);
//	        fos.close();
			
			String processInstanceId = task.getProcessInstanceId();
			// formService.submitTaskForm(taskId, properties);
			runtimeService.setVariable(processInstanceId, "newArticleDto", dto);
			formService.submitTaskForm(taskId, new HashMap<String, Object>());
//			taskService.complete(taskId);

			
	        return new ResponseEntity<NewMagazineFormEditorsReviewersRequestDto>(HttpStatus.OK);
	    }
		
		@GetMapping(path = "/updateArticleChangesStart/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity<UpdateArticleChangesDto> updateArticleChangesStart(@PathVariable String taskId) {
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			String proccessInstanceId = task.getProcessInstanceId();
			
						
			UpdateArticleChangesDto requestDto = (UpdateArticleChangesDto) runtimeService.getVariable(proccessInstanceId, "updateArticleChangesDto");
			//runtimeService.removeVariable(proccessInstanceId, "updateArticleChangesDto");
			
	        return new ResponseEntity<UpdateArticleChangesDto>(requestDto, HttpStatus.OK);
	    }	
		
		@PutMapping(path = "/updateArticleChangesPut/{taskId}", produces = "application/json")
	    public @ResponseBody ResponseEntity updateArticleChangesPut(@RequestBody UpdateArticleChangesDto dto, @PathVariable String taskId) throws IOException {
			
			
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			
//			Decoder decoder = Base64.getDecoder();
//	        byte[] decodedByte = decoder.decode(dto.getFile().split(",")[1]);
//	        FileOutputStream fos = new FileOutputStream("MyAudio.webm");
//	        fos.write(decodedByte);
//	        fos.close();
			
			String processInstanceId = task.getProcessInstanceId();
			// formService.submitTaskForm(taskId, properties);
			runtimeService.setVariable(processInstanceId, "authorMessage", dto.getAuthorsMessage());
			runtimeService.setVariable(processInstanceId, "newArticleDto", dto.getNewAarticleResponseDto());
			taskService.complete(taskId);

			
	        return new ResponseEntity<NewMagazineFormEditorsReviewersRequestDto>(HttpStatus.OK);
	    }
		
		@GetMapping(path = "/generateDoi", produces = "application/json")
	    public @ResponseBody ResponseEntity<?> generateDoi() throws IOException {
			articleService.generateDoi();
			return new ResponseEntity<>(HttpStatus.OK);
		}
}


