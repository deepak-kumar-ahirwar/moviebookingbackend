package com.moviebookingapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {

	@Id
	private String userId;
	@NotBlank(message = "firstName is mandatory")
	private String firstName;
	@NotBlank(message = "lastName is mandatory")
	private String lastName;

	@NotBlank(message = "UserName is mandatory")
	private String userName;
	private String roleName;

	@NotBlank(message = "email is mandatory")
	private  String email;

	@NotBlank(message = "Contact number is mandatory")
	private  String contactNumber;

	@NotBlank(message = "Password is mandatory")
	private String password;

	private int securityQuestion;
	private String securityAnswer;

}
