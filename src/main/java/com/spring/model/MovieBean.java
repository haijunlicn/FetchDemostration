package com.spring.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MovieBean {

	private int id;

	@NotEmpty(message = "Title must not be empty.")
	@Size(max = 150, message = "Title must not exceed 150 characters.")
	private String title;

	@NotEmpty(message = "Description must not be empty.")
	@Size(max = 1000, message = "Description must not exceed 1000 characters.")
	private String description;

	@NotNull(message = "Duration must not be null.")
	@Min(value = 1, message = "Duration must be at least 1 minute.")
	private int duration;

	private String previewUrl;
	private double rating;
	private int status;

	private Timestamp createdDate;
	private Timestamp updatedDate;
	private String formattedCasts;
	private String formattedCategories;
	private String formattedPosters;

	@NotEmpty(message = "Category list must not be empty.")
	private List<Integer> selectedCateIdList = new ArrayList<>();
	private List<CategoryBean> selectedCategoryList = new ArrayList<>();

	private List<CastBean> selectedCastList = new ArrayList<>();
	private List<Integer> selectedCastIdList = new ArrayList<>();

	private List<MovieImageBean> uploadedImgs = new ArrayList<>();
	private List<Integer> markedDeleteImgList = new ArrayList<>();

	private List<MovieCinemaShowtimeBean> airingDateList = new ArrayList<>();

	public boolean isCurrentlyAiring() {
		Date now = new Date();
		for (MovieCinemaShowtimeBean movieCinema : airingDateList) {
			if (now.after(movieCinema.getStartDate()) && now.before(movieCinema.getEndDate())) {
				return true;
			}
		}
		return false;
	}

	// Method to get formatted duration (hr:min format)
	public String getFormattedDuration() {
		int hours = duration / 60;
		int minutes = duration % 60;
		return String.format("%dh %dm", hours, minutes); // Format as "2h 45m"
	}

	public String getShortDescription() {
		return description.length() > 100 ? description.substring(0, 100) + "..." : description;
	}

}
