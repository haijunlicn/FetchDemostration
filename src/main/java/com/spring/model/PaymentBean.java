package com.spring.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PaymentBean {
	
	private int id;
	private String method_name;
	private String acc_num;
	private String qr_path;
	private int status;
}
