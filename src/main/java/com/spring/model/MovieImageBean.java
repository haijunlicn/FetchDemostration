package com.spring.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieImageBean {
	private int id;
	private int cinemaId;
	private String imgUrl;
	private Timestamp createdDate;
}
