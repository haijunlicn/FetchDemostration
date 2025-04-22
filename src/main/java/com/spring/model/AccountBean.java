package com.spring.model;

import java.sql.Timestamp;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AccountBean {
	private int id;

	@NotEmpty(message = "Name is required!")
	private String name;

	@NotEmpty(message = "Email is required!")
	@Email(message = "Invalid email format!")
	private String email;

	@NotEmpty(message = "Password is required!")
	@Size(min = 6, message = "Password must be at least 6 characters long!")
	private String password;

	private String profilePath;
	private int roleId;
	private Integer assignedCinemaId;
	private String assignedCinemaName;
	private int status;
	private Timestamp createdDate;
	private Timestamp updatedDate;

	private String roleTest;
}
