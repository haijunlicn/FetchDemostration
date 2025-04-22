package com.spring.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SeatBean {
	private Integer id;
	private int cinemaId;

	@NotEmpty(message = "Seat name is required!")
	private String seatNum;

	@NotNull(message = "Seat row is required!")
	@Min(value = 1, message = "Row must be greater than 0!")
	private Integer row;

	@NotNull(message = "Seat column is required!")
	@Min(value = 1, message = "Column must be greater than 0!")
	private Integer col;

	@NotNull(message = "Seat type is required!")
	private Integer seatTypeId;
	private String seatTypeName;
	private Double seatSize;

	@NotNull(message = "Price is required!")
	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
	private Double price;

	@NotEmpty(message = "Currency type is required!")
	private String currencyType;

	@NotNull(message = "Seat status is required!")
	@Min(value = 0, message = "Status must be 0 or 1")
	@Max(value = 1, message = "Status must be 0 or 1")
	private int status;
}
