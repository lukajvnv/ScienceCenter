package com.project.service.camunda.task;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.NewMagazineFormResponseDto;
import com.project.model.session.NewMagazineRequestSession;
import com.project.repository.NewMagazineRequestRepo;

@Service
public class AddToKpInit implements TaskListener {
	
	@Autowired
	private NewMagazineRequestRepo repo;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub

		String taskId = delegateTask.getId();
		String processId = delegateTask.getProcessInstanceId();
		
		NewMagazineFormResponseDto newMagazineDto = (NewMagazineFormResponseDto) delegateTask.getExecution().getVariable("newMagazineBasicInfo");

		long magId = newMagazineDto.getMagazineDbId();
		
		NewMagazineRequestSession req = new NewMagazineRequestSession();
		req.setMagazineId(magId);
		req.setProcessId(processId);
		req.setTaskId(taskId);
		
		repo.save(req);
	}

}
