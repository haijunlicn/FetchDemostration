function updateMovieDetails() {
	let movieDropdown = document.getElementById("movieId");
	let selectedOption = movieDropdown.options[movieDropdown.selectedIndex];

	let detailsContainer = document.getElementById("movieDetailsContainer");
	let posterContainer = document.getElementById("moviePosterContainer");

	if (!selectedOption.value) {
		detailsContainer.style.display = "none"; // Hide if no movie is selected
		return;
	}

	let title = selectedOption.getAttribute("data-title") || "";
	let duration = selectedOption.getAttribute("data-duration") || "N/A";
	let casts = selectedOption.getAttribute("data-casts") || "";
	let categories = selectedOption.getAttribute("data-category") || "";
	let posters = selectedOption.getAttribute("data-posters") || "";

	document.getElementById("movieTitle").innerText = title;
	document.getElementById("movieDuration").innerText = duration + " mins";
	document.getElementById("movieCasts").innerText = casts ? casts.replace(/,/g, ", ") : "N/A";
	document.getElementById("movieCategories").innerText = categories ? categories.replace(/,/g, ", ") : "N/A";

	posterContainer.innerHTML = ""; // Clear previous slides

	if (posters) {
		let posterUrls = posters.split(",");
		posterUrls.forEach((url, index) => {
			let slide = document.createElement("div");
			slide.className = "carousel-item" + (index === 0 ? " active" : "");

			let imgElement = document.createElement("img");
			imgElement.src = url.trim();
			imgElement.className = "d-block mx-auto";
			imgElement.style.maxHeight = "150px";
			imgElement.style.borderRadius = "5px";
			imgElement.style.objectFit = "contain";

			slide.appendChild(imgElement);
			posterContainer.appendChild(slide);
		});
	}

	detailsContainer.style.display = "block"; // Show details when a movie is selected
}

document.addEventListener("DOMContentLoaded", function() {
	
	updateMovieDetails();
	
	const currentMovieId = document.getElementById("currentMcsId").value;
	const someExcludedMovieId = parseInt(currentMovieId, 10) || null;
	console.log("current mcs id : " + someExcludedMovieId);
	const startDateInput = document.getElementById("startDate");
	const endDateInput = document.getElementById("endDate");
	const showtimeContainers = document.querySelectorAll(".showtime-container");
	const disabledShowtimeInfoContainer = document.getElementById("disabledShowtimeInfoContainer");
	const showtimeDisableMessage = document.getElementById("showtimeDisableMessage");

	function updateShowtimeAvailability() {
		const startDate = new Date(startDateInput.value);
		const endDate = new Date(endDateInput.value);

		if (isNaN(startDate) || isNaN(endDate)) {
			// Enable all showtimes if dates are invalid
			showtimeContainers.forEach(container => {
				const checkbox = container.querySelector(".hidden-checkbox");
				checkbox.disabled = false;
				container.querySelector(".showtime-card").style.backgroundColor = ""; // Reset background
				container.querySelector(".showtime-card").style.opacity = 1;
			});
			disabledShowtimeInfoContainer.style.display = "none";
			return;
		}

		let showtimeDisabledInfo = "<ul>";
		let hasDisabledShowtimes = false;

		// Step 1: Gather all conflicts
		let conflictData = {};

		showtimeContainers.forEach(container => {
			const checkbox = container.querySelector(".hidden-checkbox");
			const showtimeId = checkbox.value;
			let isAvailable = true;

			assignedMovies.forEach(movie => {
				const movieStartDate = new Date(movie.startDate);
				const movieEndDate = new Date(movie.endDate);
				const movieTitle = movie.movieObj.title;

				if (startDate <= movieEndDate && endDate >= movieStartDate && movie.id != someExcludedMovieId) {
					movie.showtimeList.forEach(showtime => {
						if (showtime.id == showtimeId) {
							isAvailable = false;
							hasDisabledShowtimes = true;

							if (!conflictData[movieTitle]) {
								conflictData[movieTitle] = [];
							}
							conflictData[movieTitle].push({ hours: showtime.startHour, dateRange: { start: movieStartDate, end: movieEndDate } });
						}
					});
				}
			});

			// Step 2: Disable unavailable showtimes
			checkbox.disabled = !isAvailable;
			const showtimeCard = container.querySelector(".showtime-card");

			if (!isAvailable) {
				checkbox.checked = false;
				showtimeCard.style.backgroundColor = "#c4c4c4";
				showtimeCard.style.opacity = 0.7;
			} else {
				showtimeCard.style.backgroundColor = "";
				showtimeCard.style.opacity = 1;
			}
		});

		// Step 3: Group and Format date ranges and showtimes
		const formatToAMPM = (timeStr) => {
			const [hours, minutes] = timeStr.split(':').map(Number);
			const ampm = hours >= 12 ? 'PM' : 'AM';
			const formattedHours = hours % 12 || 12; // Convert 0-23 to 1-12
			return `${formattedHours}:${minutes.toString().padStart(2, '0')} ${ampm}`;
		};

		Object.keys(conflictData).forEach(movieTitle => {
			let uniqueHours = [...new Set(conflictData[movieTitle].map(conflict => conflict.hours))].sort();
			let formattedHours = uniqueHours.map(formatToAMPM); // Convert to AM/PM format

			// Step 4: Group by date range
			let groupedDateRanges = [];
			conflictData[movieTitle].forEach(conflict => {
				const dateRangeString = `${conflict.dateRange.start.toLocaleDateString('en-GB')} to ${conflict.dateRange.end.toLocaleDateString('en-GB')}`;
				if (!groupedDateRanges.includes(dateRangeString)) {
					groupedDateRanges.push(dateRangeString);
				}
			});

			// Combine grouped date ranges and formatted hours into the message
			showtimeDisabledInfo += `
			  <li style="padding: 5px 10px; border-radius: 5px; font-size: 0.875rem;">
			    <span style="color: #dc3545; font-weight: bold; font-size: 1rem;">${formattedHours.join(', ')}</span> 
			    showtime(s) are unavailable for the following date ranges: 
			    <strong style="font-size: 0.875rem;">${groupedDateRanges.join(', ')}</strong> due to 
			    <strong style="color: #007bff; font-size: 0.875rem;">${movieTitle}</strong>.
			  </li>`;

		});

		showtimeDisabledInfo += "</ul>";

		// Step 5: Display the message
		if (hasDisabledShowtimes) {
			showtimeDisableMessage.innerHTML = showtimeDisabledInfo;
			disabledShowtimeInfoContainer.style.display = "block";
		} else {
			disabledShowtimeInfoContainer.style.display = "none";
		}
	}

	// Update availability when start date or end date is changed
	startDateInput.addEventListener("change", updateShowtimeAvailability);
	endDateInput.addEventListener("change", updateShowtimeAvailability);
	document.getElementById("showtimeList").addEventListener("change", function(event) {
	    if (event.target.type === "checkbox") {
	        console.log("Checkbox changed:", event.target.value, "Checked:", event.target.checked);
			updateShowtimeAvailability();
	    }
	});


});
