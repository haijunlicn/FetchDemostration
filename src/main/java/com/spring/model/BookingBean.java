package com.spring.model;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BookingBean {

	private Integer id;
	private Integer cusId;
	private String cusName;
	private Integer mcId;
	private Integer mcsId;
	private McsBean mcsObj;
	private Integer movieId;
	private String movieTitle;
	private Integer cinemaId;
	private String cinemaName;
	private Integer selectedShowtimeId;
	private ShowtimeBean selectedShowtimeObj;
	private double totalAmount;
	private Date bookingDate;
	private String bookingDateStr;
	private String screenshotPath;
	private Integer status;
	private Integer paymentMethodId;
	private String paymentMethodName;
	private Integer adminId;
	private Date createdDate;
	private List<Integer> bookedSeatIdList;
	private List<SeatBean> bookedSeatList;

	public double calculateTotalAmount() {
		if (bookedSeatList == null || bookedSeatList.isEmpty()) {
			return 0;
		}
		return bookedSeatList.stream().mapToDouble(SeatBean::getPrice).sum();
	}

}
