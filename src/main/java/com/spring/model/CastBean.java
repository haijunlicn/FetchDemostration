package com.spring.model;

import java.sql.Date;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CastBean {
	private int id;

	@NotEmpty(message = "name is required")
	private String name;

	@NotNull(message = "Birthdate must be chosen.")
	@PastOrPresent(message = "Birthdate must be in the past.")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate localBirthdate;
	private Date birthdate;

	@NotEmpty(message = "nationality is required")
	private String nationality;

	@NotEmpty(message = "Biography is required")
	@Size(max = 1500, message = "Biography must not exceed 1500 characters")
	private String biography;

	@NotEmpty(message = "gender is required")
	private String gender;
	
	private String profilePath;
	private int status;
}
