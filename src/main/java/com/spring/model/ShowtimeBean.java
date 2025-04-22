package com.spring.model;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShowtimeBean {
	private int id;

	@NotNull(message = "Enter a start hour!")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime localStartHour;

	@NotNull(message = "Enter an end hour!")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime localEndHour;

	private Time startHour;
	private Time endHour;
	private int status;

	// Method to get formatted showtime in "hh:mm a - hh:mm a" format
	public String getFormattedShowtime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

		LocalTime localStartTime = startHour.toLocalTime();
		LocalTime localEndTime = endHour.toLocalTime();

		String formattedStartHour = localStartTime.format(formatter);
		String formattedEndHour = localEndTime.format(formatter);

		return formattedStartHour + " - " + formattedEndHour;
	}

	// Method to format Time to AM/PM
	public static String formatTimeToAMPM(Time time) {
		LocalTime localTime = time.toLocalTime(); // Convert Time to LocalTime
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
		return localTime.format(formatter); // Format to AM/PM
	}

	public boolean isValidHourRange() {
		if (localStartHour == null || localEndHour == null) {
			return true; // Let @NotNull handle these cases
		}
		Duration duration = Duration.between(localStartHour, localEndHour);
		return localEndHour.isAfter(localStartHour) && duration.toHours() < 4;
	}
}