package com.project.service.camunda.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.impl.form.type.EnumFormType;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.NewArticleRequestDto;
import com.project.dto.ScienceAreaDto;
import com.project.dto.TermDto;
import com.project.dto.UserDto;
import com.project.model.Magazine;
import com.project.model.ScienceArea;
import com.project.model.Term;
import com.project.model.enums.Role;
import com.project.model.user.UserSignedUp;
import com.project.repository.UnityOfWork;
import com.project.util.camunda.customType.MultiEnumFormType;
import com.project.util.camunda.customType.MultiEnumFormTypeCoAuthor;
import com.project.util.camunda.customType.MultiEnumFormTypeCoAuthorOut;

@Service
public class NewArticleFormDataInitialization implements TaskListener {

	@Autowired
	private UnityOfWork unityOfWork;
	
	@Autowired
	private FormService formService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub

		DelegateExecution execution = delegateTask.getExecution();
		String proccessId = delegateTask.getProcessInstanceId();
		String taskId = delegateTask.getId();
		
		
		
		Long magazineId = (Long) execution.getVariable("select_magazine_id");
		Magazine magazine = unityOfWork.getMagazineRepository().getOne(magazineId);
		List<ScienceArea> scienceAreas = new ArrayList<ScienceArea>(magazine.getScienceAreas());
		
		// sc areas enum datasource init
		TaskFormData formData =  formService.getTaskFormData(delegateTask.getId());
		FormField field = formData.getFormFields().stream().filter(f -> f.getId().equals("article_science_area")).findFirst().get();
		EnumFormType ft = (EnumFormType)field.getType();
		Map<String, String> map = ft.getValues();
		
		scienceAreas.forEach(scArea -> { 
		  map.put(scArea.getScienceAreaId().toString(), scArea.getScienceAreaCode() + ":" + scArea.getScienceAreaName());
		});
		
		
		
		List<UserSignedUp> coAuthors = unityOfWork.getUserSignedUpRepository().findByRoleOrderByLastName(Role.COMMON_USER);
		String userId = (String) execution.getVariable("user");
		UserSignedUp authorInitiator = unityOfWork.getUserSignedUpRepository().findByUserUsername(userId);
		coAuthors.remove(authorInitiator);
		
		List<Term> terms = unityOfWork.getTermRepository().findAll();

		// key terms enum datasource init
		FormField keyTermsFormField = formData.getFormFields().stream().filter(f -> f.getId().equals("article_key_terms")).findFirst().get();
		MultiEnumFormType keyTermsMultiEnumType = (MultiEnumFormType) keyTermsFormField.getType();
		Map<String, String> maps = keyTermsMultiEnumType.getValues();
		
		terms.forEach(term -> { 
			maps.put(term.getTermId().toString(), term.getTermName());
		});
		
		
		// co authors  enum datasource init
		FormField coAuthorsFormField = formData.getFormFields().stream().filter(f -> f.getId().equals("article_co_authors")).findFirst().get();
		MultiEnumFormTypeCoAuthor coAuthorsMultiEnumType = (MultiEnumFormTypeCoAuthor) coAuthorsFormField.getType();
		Map<String, String> coAuthorsMaps = coAuthorsMultiEnumType.getValues();
		
		coAuthors.forEach(coAuthor -> { 
			String text = new StringBuilder(coAuthor.getFirstName())
					.append(" ")
					.append(coAuthor.getLastName())
					.append(", city: ")
					.append(coAuthor.getCity())
					.append(", profession: ")
					.append(coAuthor.getVocation())
					.toString();
			coAuthorsMaps.put(coAuthor.getUserId().toString(), text);
		});
		
		
		
		
		
		List<ScienceAreaDto> scienceAreasDto = new ArrayList<ScienceAreaDto>();
		List<TermDto> termsDto = new ArrayList<TermDto>();
		List<UserDto> coAuthorsDto = new ArrayList<UserDto>();
		
		scienceAreas.stream().forEach(sc -> scienceAreasDto.add(new ScienceAreaDto(sc.getScienceAreaId(), sc.getScienceAreaName(), sc.getScienceAreaCode())));
		terms.stream().forEach(t -> termsDto.add(new TermDto(t.getTermId(), t.getTermName())));
		coAuthors.stream().forEach(c -> coAuthorsDto.add(new UserDto(c.getUserId(), c.getFirstName(), c.getLastName(), c.getEmail(), c.getCity(), c.getCountry(), c.getUserUsername(), c.getVocation())));
	
		// co authors custom enum datasource init
		FormField coAuthorsCustomFormField = formData.getFormFields().stream().filter(f -> f.getId().equals("article_co_authors_out")).findFirst().get();
		MultiEnumFormTypeCoAuthorOut coAuthorsCustomMultiEnumType = (MultiEnumFormTypeCoAuthorOut) coAuthorsCustomFormField.getType();
		Map<String, UserDto> coAuthorsCustomMaps = coAuthorsCustomMultiEnumType.getValues();
		
		coAuthorsDto.forEach(coAuthor -> { 
			coAuthorsCustomMaps.put(coAuthor.getUserId().toString(), coAuthor);
		});
		
		
		NewArticleRequestDto requestDto = new NewArticleRequestDto(taskId, proccessId, scienceAreasDto, termsDto, coAuthorsDto, null);
		
		execution.setVariable("newArticleRequestDto", requestDto);
	}

}
