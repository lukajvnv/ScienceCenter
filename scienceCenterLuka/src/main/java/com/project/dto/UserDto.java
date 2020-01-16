package com.project.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7894223130709484206L;
	
	private Long userId;
	private String firstName;
	private String lastName;
	private String email;
	private String city;
	private String country;
	private String userUsername;
	
	private String vocation;

}
