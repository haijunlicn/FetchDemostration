package com.spring.model;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MovieCinemaShowtimeBean {

	private Integer id;

	private Integer movieCinemaShowtimeId;

	@NotNull(message = "Select a movie to assign!")
	private Integer movieId;

	private MovieBean movieObj = new MovieBean();

	@NotNull(message = "Select a cinmea to assign movie!")
	private Integer cinemaId;

	private CinemaBean cinemaObj = new CinemaBean();

	@NotNull(message = "Start date must be chosen!")
	@FutureOrPresent(message = "Start date must be in the future or present")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate localStartDate;

	@NotNull(message = "End date must be chosen!")
	@FutureOrPresent(message = "End date must be in the future or present")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate localEndDate;

	private Date startDate;
	private Date endDate;

	@Size(min = 1, message = "At least one showtime must be selected.")
	private List<Integer> selectedShowtimeIdList = new ArrayList<>();

	private List<ShowtimeBean> showtimeList = new ArrayList<>();

	public boolean isValidTimeRange() {
		if (localStartDate == null || localEndDate == null) {
			return true;
		}
		return localEndDate.isAfter(localStartDate);
	}

	public List<String> getAvailableDates() {
		List<String> availableDates = new ArrayList<>();

		// Ensure that the start date is at least one day after the current date
		LocalDate currentDate = LocalDate.now();
		LocalDate validStartDate = localStartDate.isAfter(currentDate) ? localStartDate : currentDate.plusDays(1);

		// Define the desired date format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		// Iterate through the range of dates
		while (!validStartDate.isAfter(localEndDate)) {
			// Format the date to the desired format and add to the list
			availableDates.add(validStartDate.format(formatter));
			validStartDate = validStartDate.plusDays(1);
		}

		return availableDates;
	}

	/*
	 * public List<LocalDate> getAvailableDates() { List<LocalDate> availableDates =
	 * new ArrayList<>();
	 * 
	 * // Ensure that the start date is at least one day after the current date
	 * LocalDate currentDate = LocalDate.now(); LocalDate validStartDate =
	 * localStartDate.isAfter(currentDate) ? localStartDate :
	 * currentDate.plusDays(1);
	 * 
	 * // Iterate through the range of dates while
	 * (!validStartDate.isAfter(localEndDate)) { availableDates.add(validStartDate);
	 * validStartDate = validStartDate.plusDays(1); }
	 * 
	 * return availableDates; }
	 */

}
