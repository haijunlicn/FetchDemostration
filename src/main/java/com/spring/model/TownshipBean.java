package com.spring.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TownshipBean {
	private int id;
	private String name;
	private Integer stateId;
	private String stateName;
	private int status;
}
