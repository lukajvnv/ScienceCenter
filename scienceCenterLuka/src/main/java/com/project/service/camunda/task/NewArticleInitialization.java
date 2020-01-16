package com.project.service.camunda.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Service;

@Service
public class NewArticleInitialization implements ExecutionListener {

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub

		// Privremeno...
		execution.setVariable("user", "lukaAuthor");
	}

}
