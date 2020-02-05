package com.project.dto;

import java.util.List;

import org.camunda.bpm.engine.form.FormField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEditorDto {
	
	private String firstName;
	private String lastName;
	private String email;
	private String city;
	private String country;
	private String username;
	private String password;
	private String vocation;

}
