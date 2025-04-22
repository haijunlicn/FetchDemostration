package com.spring.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CinemaBean {
	private int id;

	@NotEmpty(message = "Cinema name is required!")
	private String name;

	@NotNull(message = "Choose a township!")
	private Integer townshipId;
	private String townshipName;

	@NotNull(message = "Choose a state!")
	private Integer stateId;

	/* @NotEmpty(message = "Choose a state!") */
	private String stateName;
	private int totalSeats;
	private int rowCount;
	private int colCount;
	private String lat;
	private String lon;
	private int status;
	private Timestamp createdDate;
	private Timestamp updatedDate;

	@NotEmpty(message = "Feature list must not be empty.")
	private List<Integer> selectedFeatureIdList = new ArrayList<>();
	private List<FeatureBean> selectedFeatureList = new ArrayList<>();

	private List<Integer> selectedShowtimeList = new ArrayList<Integer>();
	private List<Integer> markedDeleteImgList = new ArrayList<Integer>();
	private List<CinemaImageBean> uploadedImgs = new ArrayList<CinemaImageBean>();
	private List<MovieCinemaShowtimeBean> assignedMovies = new ArrayList<MovieCinemaShowtimeBean>();
	private List<SeatBean> seatList = new ArrayList<SeatBean>();
	private List<Integer> seatIdList = new ArrayList<Integer>();

	public double getMinSeatPrice() {
		return seatList.stream().mapToDouble(SeatBean::getPrice).min().orElse(0.0);
	}

	// Method to get maximum price of seats
	public double getMaxSeatPrice() {
		return seatList.stream().mapToDouble(SeatBean::getPrice).max().orElse(0.0);
	}
}
