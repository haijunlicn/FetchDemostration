package com.spring.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeatureBean {
	
	
	@NotNull
	private int id;
	
	@NotEmpty ( message = "name is required!")
	private String name;
	
	@NotEmpty ( message = "description is required!")
	private String description;
	
	@NotNull ( message = "status is required!")
	private int status;

}
