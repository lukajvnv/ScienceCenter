package com.project.controller;

import java.util.Set;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.model.EditorReviewerByScienceArea;
import com.project.model.Magazine;
import com.project.model.MagazineEdition;
import com.project.repository.UnityOfWork;

@Controller
@RequestMapping("/welcome")
@CrossOrigin
public class DummyController {
	@Autowired
	IdentityService identityService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	FormService formService;
	
	@Autowired
	UnityOfWork unityOfWork;
	
	@GetMapping(path = "/get", produces = "application/json")
    public @ResponseBody ResponseEntity get() {
		//provera da li korisnik sa id-jem pera postoji
		Magazine mag1 = unityOfWork.getMagazineRepository().getOne(1l);
		Set<EditorReviewerByScienceArea> r = mag1.getEditorsReviewersByScienceArea();
		String t = mag1.getName();
		
		Set<MagazineEdition> e = mag1.getMagazineEditions();
		
        
		
		return new ResponseEntity(HttpStatus.OK);
    }
	
	@GetMapping(path = "/test", produces = "application/json")
    public @ResponseBody ResponseEntity test() {
		//provera da li korisnik sa id-jem pera postoji
		Magazine mag1 = null;
		System.out.println(mag1.getName());
		
        return new ResponseEntity(HttpStatus.OK);
    }
	
	
}
