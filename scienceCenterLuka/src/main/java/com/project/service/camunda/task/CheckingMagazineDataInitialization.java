package com.project.service.camunda.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.form.type.StringFormType;
import org.camunda.bpm.engine.rest.dto.task.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.CheckingMagazineDataDto;
import com.project.dto.DisplayMagazineDto;
import com.project.dto.EditorReviewerByScienceAreaDto;
import com.project.dto.NewMagazineFormResponseDto;
import com.project.dto.ScienceAreaDto;
import com.project.model.EditorReviewerByScienceArea;
import com.project.model.Magazine;
import com.project.model.ScienceArea;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;


@Service
public class CheckingMagazineDataInitialization implements TaskListener {
	
	@Autowired
	FormService formService;
	
	@Autowired
	UnityOfWork unityOfWork;

//	@Override
//	public void notify(DelegateTask delegateTask) {
//		// TODO Auto-generated method stub
//			
//		DelegateExecution execution = delegateTask.getExecution();
//		NewMagazineFormResponseDto newMagazineDto = (NewMagazineFormResponseDto) execution.getVariable("newMagazineBasicInfo");
//		
//		
//		Magazine magazine = unityOfWork.getMagazineRepository().getOne(newMagazineDto.getMagazineDbId());
//		Set<ScienceArea> scieceAreas = magazine.getScienceAreas();
//		Set<EditorReviewerByScienceArea> editorsReviewers =  magazine.getEditorsReviewersByScienceArea();
//		Set<EditorReviewerByScienceArea> editors = editorsReviewers.stream().filter(e -> e.isEditor() == true).collect(Collectors.toSet());
//		Set<EditorReviewerByScienceArea> reviewers = editorsReviewers.stream().filter(r -> r.isEditor() == false).collect(Collectors.toSet());
//		UserSignedUp chiefEditor = magazine.getChiefEditor();
//		
//		execution.setVariable("view_name", magazine.getName());
//		execution.setVariable("view_issn_number", magazine.getISSN());
//		execution.setVariable("view_payment_option", magazine.getWayOfPayment());
//		execution.setVariable("view_membership_price", magazine.getMembershipPrice());
//		execution.setVariable("view_chief_editor", chiefEditor.getFirstName() + " " + chiefEditor.getLastName());
//		
////		execution.setVariable("view_editors", editors.iterator().next().getEditorReviewer().getUserUsername());
//
//		
////		FormField fieldChiefEditor = taskFormData.getFormFields().stream().filter(f -> f.getId().equals("view_chief_editor")).findFirst().get();
////		Object value = fieldChiefEditor.getDefaultValue();
//		StringFormType type = (StringFormType) fieldChiefEditor.getType();
////		value = chiefEditor.getFirstName() + " " + chiefEditor.getLastName();
//		
//		TaskFormData taskFormData = formService.getTaskFormData(delegateTask.getId());
//		FormField fieldEditors = taskFormData.getFormFields().stream().filter(f -> f.getId().equals("view_editors")).findFirst().orElse(null);
//		EnumFormType enumTypeEditors = (EnumFormType) fieldEditors.getType();
//		Map<String, String> enumTypeValuesEditors = enumTypeEditors.getValues();
////		enumTypeValuesEditors.clear();
//		editors.stream().forEach(e -> {	
//			StringBuilder text =new StringBuilder("name:");  
//			text.append(e.getEditorReviewer().getFirstName())
//				.append(" ")
//				.append(e.getEditorReviewer().getLastName())
//				.append(", area:")
//				.append(e.getScienceArea().getScienceAreaName());
//			
//			//enumTypeValuesEditors.put(e.getEditorByScArId().toString(), text.toString());
//			enumTypeValuesEditors.put(e.getEditorReviewer().getUserUsername(), text.toString());
//		});
//		
//		
//		FormField fieldReviewers = taskFormData.getFormFields().stream().filter(f -> f.getId().equals("view_reviewers")).findFirst().orElse(null);
//		EnumFormType enumTypeReviewers = (EnumFormType) fieldReviewers.getType();
//		Map<String, String> enumTypeValuesReviewers = enumTypeReviewers.getValues();
////		enumTypeValuesReviewers.clear();
//		reviewers.stream().forEach(e -> {	
//			StringBuilder text =new StringBuilder("name:");  
//			text.append(e.getEditorReviewer().getFirstName())
//				.append(" ")
//				.append(e.getEditorReviewer().getLastName())
//				.append(", area:")
//				.append(e.getScienceArea().getScienceAreaName());
//			
//			enumTypeValuesReviewers.put(e.getEditorByScArId().toString(), text.toString());
//		});
//		
//		FormField fieldScAreas = taskFormData.getFormFields().stream().filter(f -> f.getId().equals("view_science_area")).findFirst().orElse(null);
//		EnumFormType enumTypeScAreas = (EnumFormType) fieldScAreas.getType();
//		Map<String, String> enumTypeValuesScAreas = enumTypeScAreas.getValues();
////		enumTypeValuesScAreas.clear();
//		scieceAreas.stream().forEach(sc -> {	
//			StringBuilder text = new StringBuilder("code:");  
//			text.append(sc.getScienceAreaCode())
//				.append(", name:")
//				.append(sc.getScienceAreaName());
//			
//			enumTypeValuesScAreas.put(sc.getScienceAreaId().toString(), text.toString());
//		});
//	}
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
			
		DelegateExecution execution = delegateTask.getExecution();
		NewMagazineFormResponseDto newMagazineDto = (NewMagazineFormResponseDto) execution.getVariable("newMagazineBasicInfo");
		
		
		Magazine magazine = unityOfWork.getMagazineRepository().getOne(newMagazineDto.getMagazineDbId());
		Set<ScienceArea> scieceAreas = magazine.getScienceAreas();
		
		Set<EditorReviewerByScienceArea> editorsReviewers =  magazine.getEditorsReviewersByScienceArea();
		Set<EditorReviewerByScienceArea> editors = editorsReviewers.stream().filter(e -> e.isEditor() == true).collect(Collectors.toSet());
		Set<EditorReviewerByScienceArea> reviewers = editorsReviewers.stream().filter(r -> r.isEditor() == false).collect(Collectors.toSet());
		UserSignedUp chiefEditor = magazine.getChiefEditor();
		
//		execution.setVariable("view_name", magazine.getName());
//		execution.setVariable("view_issn_number", magazine.getISSN());
//		execution.setVariable("view_payment_option", magazine.getWayOfPayment());
//		execution.setVariable("view_membership_price", magazine.getMembershipPrice());
//		execution.setVariable("view_chief_editor", chiefEditor.getFirstName() + " " + chiefEditor.getLastName());
		
		List<ScienceAreaDto> scienceAreasDto = new ArrayList<ScienceAreaDto>();
		List<EditorReviewerByScienceAreaDto> reviewersDto = new ArrayList<EditorReviewerByScienceAreaDto>();
		List<EditorReviewerByScienceAreaDto> editorsDto = new ArrayList<EditorReviewerByScienceAreaDto>();
		
		reviewers.stream().forEach(e -> {	
			UserSignedUp chief = e.getEditorReviewer();
			ScienceArea sc = e.getScienceArea();
			reviewersDto.add(new EditorReviewerByScienceAreaDto(e.getEditorByScArId(), e.isEditor(), 
					new com.project.dto.UserDto(chief.getUserId(), chief.getFirstName(), chief.getLastName(), chief.getEmail(), chief.getCity(), chief.getCountry(), chief.getUserUsername(), chief.getVocation()), new ScienceAreaDto(sc.getScienceAreaId(), sc.getScienceAreaName(), sc.getScienceAreaCode()),
					null, 0d, 0d));
		});
		
		editors.stream().forEach(e -> {	
			UserSignedUp chief = e.getEditorReviewer();
			ScienceArea sc = e.getScienceArea();
			editorsDto.add(new EditorReviewerByScienceAreaDto(e.getEditorByScArId(), e.isEditor(), 
					new com.project.dto.UserDto(chief.getUserId(), chief.getFirstName(), chief.getLastName(), chief.getEmail(), chief.getCity(), chief.getCountry(), chief.getUserUsername(), chief.getVocation()), new ScienceAreaDto(sc.getScienceAreaId(), sc.getScienceAreaName(), sc.getScienceAreaCode()),
					null, 0d, 0d));
		});
		
		scieceAreas.stream().forEach(s -> {
			scienceAreasDto.add(new ScienceAreaDto(s.getScienceAreaId(), s.getScienceAreaName(), s.getScienceAreaCode()));
		});
		
		com.project.dto.UserDto chiefDto = new com.project.dto.UserDto(chiefEditor.getUserId(), chiefEditor.getFirstName(), chiefEditor.getLastName(), chiefEditor.getEmail(), chiefEditor.getCity(), chiefEditor.getCountry(), chiefEditor.getUserUsername(), chiefEditor.getVocation());

		DisplayMagazineDto m = new DisplayMagazineDto(magazine.getMagazineId(), magazine.getISSN(), magazine.getName(), scienceAreasDto, magazine.getWayOfPayment(), magazine.getMembershipPrice(), chiefDto, editorsDto, reviewersDto);
		CheckingMagazineDataDto checkingMagazineDto = new CheckingMagazineDataDto(delegateTask.getId(), m, null, null);
		
    	execution.setVariable("checkingMagazineDto", checkingMagazineDto);
	}

}
