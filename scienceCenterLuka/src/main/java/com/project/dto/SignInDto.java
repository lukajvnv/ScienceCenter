package com.project.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto {

//	private String username;
//	private String password;
	
	@NotBlank
    @Size(min=3, max = 60)
    private String username;

    @NotBlank
    @Size(min = 3, max = 40)
    private String password;
}
