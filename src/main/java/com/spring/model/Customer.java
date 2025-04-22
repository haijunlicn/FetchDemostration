package com.spring.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the id field
	private Integer id;

	@NotEmpty(message = "Name is required!")
	private String name;

	@NotEmpty(message = "Email is required!")
	@Email(message = "Invalid email format!")
	private String email;

	@NotEmpty(message = "Password is required!")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Password must be at least 8 characters long and include an uppercase letter, a lowercase letter, a digit, and a special character.")
	private String password;

	private String phone;
	private Integer roleId;
	private Integer assignedCinema;
}
