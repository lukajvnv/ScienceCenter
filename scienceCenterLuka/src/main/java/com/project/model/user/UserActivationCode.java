package com.project.model.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

//@Entity
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@SuperBuilder(toBuilder = true)
//public class UserActivationCode {
//	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	@Column
//	private String email;
//	
//	@Column
//	private String code;
//
//}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserActivationCode implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5489723040558955574L;

	private Long id;
	
	private String userId;

	private String email;
	
	private String code;

}
