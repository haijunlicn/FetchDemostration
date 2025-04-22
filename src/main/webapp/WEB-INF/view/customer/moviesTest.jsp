<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Find Your Perfect Seat</title>
<link href="resources/css/movieFilter.css" rel="stylesheet">
<style>
.swal2-popup {
	background: #1a1a1a !important;
	color: #fff !important;
	border: 2px solid #dc3545 !important;
}

.swal2-title {
	color: #dc3545 !important;
	font-weight: 600 !important;
}

.swal2-html-container {
	color: #fff !important;
}

.swal2-confirm {
	background: #dc3545 !important;
	color: #fff !important;
	border: none !important;
	box-shadow: none !important;
}

.swal2-confirm:hover {
	background: #bb2d3b !important;
}

.swal2-cancel {
	background: #343a40 !important;
	color: #fff !important;
	border: 1px solid #dc3545 !important;
}

.swal2-cancel:hover {
	background: #23272b !important;
}

.swal2-icon.swal2-info {
	border-color: #dc3545 !important;
	color: #dc3545 !important;
}
</style>
</head>
<body class="bg-dark">

	<jsp:include page="../cusSidebar.jsp"></jsp:include>

	<div class="main clearfix position-relative">
		<jsp:include page="../cusHeader.jsp"></jsp:include>

		<!-- Hero Section with Quick Select -->
		<section class="hero-section">
			<div class="container">
				<h1>Find Your Perfect Cinema Experience</h1>
				<p>Select your preferred cinema, movie and showtime</p>
			</div>
		</section>

		<!-- Quick Select Box -->
		<div class="container quick-select-container">
			<div class="quick-select">

				<form:form class="row g-2" modelAttribute="mcsObj"
					action="showFilteredMovieShowtimes" method="get">
					<div class="col">
						<form:select class="form-select" path="cinemaId">
							<form:option value="">Select a cinema</form:option>
							<form:options items="${cinemaList}" itemValue="id"
								itemLabel="name" />
						</form:select>

					</div>

					<div class="col">
						<form:select class="form-select" path="movieId">
							<form:option value="">Select a movie</form:option>
							<form:options items="${movieList}" itemValue="id"
								itemLabel="title" />
						</form:select>
					</div>

					<div class="col">
						<div class="input-group date">
							<input type="text" name="selectedDate" id="dateInput"
								class="form-control datepicker" placeholder="DD/MM/YYYY"
								value="${selectedDate}" /> <span class="input-group-text"><i
								class="fas fa-calendar-alt"></i></span>
						</div>
					</div>

					<div class="col">
						<c:choose>
							<c:when test="${not empty showtimeList}">
								<select class="form-select" name="selectedShowtimeId">
									<option value="">Select a showtime</option>
									<c:forEach items="${showtimeList}" var="showtime">
										<option value="${showtime.id}"
											${showtime.id eq selectedShowtimeId ? 'selected="selected"' : ''}>
											${showtime.getFormattedShowtime()}</option>
									</c:forEach>
								</select>
							</c:when>
							<c:otherwise>
								<p>No showtimes available</p>
							</c:otherwise>
						</c:choose>
					</div>

					<div class="col-auto">
						<button type="submit" class="search-btn">
							<i class="fas fa-search"></i> Search
						</button>
					</div>
				</form:form>

			</div>
		</div>

		<!-- Content Section -->
		<div class="container">
			<div class="content-wrapper">

				<c:forEach var="mcs" items="${mcsObjList}">
					<div class="movie-showtime-card">
						<div class="movie-showtime-poster">
							<img src="${mcs.movieObj.uploadedImgs[0].imgUrl}"
								alt="Movie Poster">
						</div>
						<div class="movie-info">
							<div>
								<div class="movie-title-container">
									<%-- 									<h2 class="movie-title"
										onclick="showMovieDetails('${mcs.movieObj.title}')">${mcs.movieObj.title}
									</h2> --%>

									<h2 class="movie-title" data-title="${mcs.movieObj.title}"
										data-duration="${mcs.movieObj.getFormattedDuration()}"
										data-categories="<c:forEach var='cate' items='${mcs.movieObj.selectedCategoryList}' varStatus='status'>${cate.name}<c:if test='${!status.last}'>, </c:if></c:forEach>"
										data-description="${mcs.movieObj.description}"
										data-rating="${mcs.movieObj.rating}"
										data-preview-url="${mcs.movieObj.previewUrl}"
										onclick="showMovieDetails(this)">${mcs.movieObj.title}</h2>



									<button class="trailer-btn"
										onclick="showTrailer('${mcs.movieObj.previewUrl}')">
										<i class="fas fa-play"></i> Watch Trailer
									</button>

								</div>
								<div class="movie-meta">
									<span class="meta-item"> <i class="fas fa-clock"></i>
										${mcs.movieObj.getFormattedDuration()}
									</span> <span class="meta-item"><i class="fas fa-film"></i> <c:forEach
											var="cate" items="${mcs.movieObj.selectedCategoryList}"
											varStatus="status">
											<span> ${cate.name} <c:if test="${!status.last}">, </c:if>
											</span>
										</c:forEach> </span>
								</div>
								<p class="showing-dates">Showing: ${mcs.startDate} -
									${mcs.endDate}</p>
								<p class="price-range">Tickets:
									$${mcs.cinemaObj.getMinSeatPrice()} -
									$${mcs.cinemaObj.getMaxSeatPrice()}</p>
							</div>
							<div>
								<div class="showtime-list">
									<c:forEach var="showtime" items="${mcs.showtimeList}">
										<div class="time-slot">${showtime.getFormattedShowtime()}</div>
									</c:forEach>
								</div>
							</div>
						</div>
						<div class="cinema-info">
							<div>
								<h3 class="cinema-name"
									onclick="showCinemaDetails('${mcs.cinemaObj}')">${mcs.cinemaObj.name}</h3>
								<p class="cinema-address">${mcs.cinemaObj.townshipName},
									${mcs.cinemaObj.stateName }</p>
								<div class="cinema-features">
									<c:forEach var="feature"
										items="${mcs.cinemaObj.selectedFeatureList}">
										<span class="feature-badge">${feature.name}</span>
									</c:forEach>
								</div>
							</div>
							<div>
								<a
									href="booking/bookSeats/${mcs.id}?selectedDate=${selectedDate}&selectedShowtimeId=${selectedShowtimeId}"
									class="book-btn"> <i class="fas fa-ticket-alt"></i> Book
									Now
								</a>
							</div>

						</div>
					</div>
				</c:forEach>

			</div>
		</div>

		<!-- Movie Details Modal -->
		<div id="movieModal" class="modal">
			<div class="modal-content">
				<span class="close-modal" onclick="closeMovieModal()">&times;</span>
				<div id="movieDetails"></div>
			</div>
		</div>

		<!-- Cinema Details Modal -->
		<div id="cinemaModal" class="modal">
			<div class="modal-content">
				<span class="close-modal" onclick="closeCinemaModal()">&times;</span>
				<div id="cinemaDetails"></div>
			</div>
		</div>

		<!-- Trailer Modal -->
		<div id="trailerModal" class="modal">
			<div class="modal-content trailer-modal-content">
				<span class="close-modal" onclick="closeTrailerModal()">&times;</span>
				<div class="video-container">
					<div>
						<video id="trailerVideo" width="100%" height="100%" controls
							autoplay>
							<source id="videoSource" type="video/mp4">
						</video>
					</div>
				</div>
			</div>
		</div>

		<jsp:include page="../cusFooter.jsp"></jsp:include>
	</div>



	<script>
		// Movie Modal Functions
		function showMovieDetails(element) {
    const modal = document.getElementById('movieModal');
    const detailsDiv = document.getElementById('movieDetails');

    // Extract movie data from the clicked element
    const movie = {
        title: element.getAttribute('data-title'),
        duration: element.getAttribute('data-duration'),
        categories: element.getAttribute('data-categories'),
        description: element.getAttribute('data-description'),
        rating: element.getAttribute('data-rating'),
        previewUrl: element.getAttribute('data-preview-url')
    };

    // Create dynamic images
    let imagesHtml = "";
    let dotsHtml = "";
    movie.uploadedImgs.forEach((img, index) => {
        const isActive = index === 0 ? 'active' : ''; // Use JavaScript for conditional logic
        imagesHtml += `<img src="${img.url}" class="slider-images ${isActive}" alt="Movie Image ${index + 1}">`;
        dotsHtml += `<span class="dot ${isActive}" onclick="currentSlide(${index + 1})"></span>`;
    });

    // Insert dynamic data into modal
    detailsDiv.innerHTML = `
        <h2>${movie.title}</h2>
        <div style="display: flex; gap: 20px; margin: 20px 0;">
            <div class="movie-image-slider">
                ${imagesHtml}
                <button class="slider-nav prev-btn" onclick="changeSlide(-1)"><i class="fas fa-chevron-left"></i></button>
                <button class="slider-nav next-btn" onclick="changeSlide(1)"><i class="fas fa-chevron-right"></i></button>
                <div class="slider-dots">${dotsHtml}</div>
            </div>
            <div>
                <p><strong>Director:</strong> ${movie.director}</p>
                <p><strong>Cast:</strong> ${movie.formattedCasts}</p>
                <p><strong>Synopsis:</strong> ${movie.description}</p>
                <p><strong>Duration:</strong> ${movie.duration}</p>
                <p><strong>Rating:</strong> ${movie.rating}/10</p>
                <p><strong>Genre:</strong> ${movie.categories}</p>
            </div>
        </div>
    `;

    // Show modal
    modal.style.display = 'block';

    // Initialize the slider
    setTimeout(() => {
        slideIndex = 1;
        showSlides(slideIndex);
    }, 100);
}

let slideIndex = 1;
showSlides(slideIndex);

function changeSlide(n) {
    showSlides(slideIndex += n);
}

function currentSlide(n) {
    showSlides(slideIndex = n);
}

function showSlides(n) {
    let slides = document.getElementsByClassName("slider-images");
    let dots = document.getElementsByClassName("dot");

    // If slides or dots elements are not found, return early
    if (slides.length === 0 || dots.length === 0) {
        console.log("Slides or dots elements not found.");
        return;
    }

    // Adjust slideIndex if necessary
    if (n > slides.length) {
        slideIndex = 1;
    }
    if (n < 1) {
        slideIndex = slides.length;
    }

    // Loop to remove 'active' class from all slides and dots
    Array.from(slides).forEach(slide => slide.classList.remove("active"));
    Array.from(dots).forEach(dot => dot.classList.remove("active"));

    // Add 'active' class to the current slide and dot
    slides[slideIndex - 1].classList.add("active");
    dots[slideIndex - 1].classList.add("active");
}

function closeMovieModal() {
    document.getElementById('movieModal').style.display = 'none';
}

		// Cinema Modal Functions
		function showCinemaDetails(cinemaName) {
			const modal = document.getElementById('cinemaModal');
			const detailsDiv = document.getElementById('cinemaDetails');
			// Here you would typically make an AJAX call to get cinema details
			detailsDiv.innerHTML = `
				<h2>${cinemaName}</h2>
				<div style="margin: 20px 0;">
					<p><strong>Address:</strong> 123 Movie Street, Entertainment District</p>
					<p><strong>Contact:</strong> (555) 123-4567</p>
					<p><strong>Features:</strong></p>
					<ul>
						<li>IMAX Theater</li>
						<li>Dolby Atmos Sound System</li>
						<li>4K Digital Projection</li>
						<li>VIP Seating Available</li>
						<li>Concession Stand</li>
						<li>Wheelchair Accessible</li>
					</ul>
					<p><strong>Parking:</strong> Free parking available</p>
					<p><strong>Operating Hours:</strong> 10:00 AM - 12:00 AM</p>
				</div>
			`;
			modal.style.display = 'block';
		}

		function closeCinemaModal() {
			document.getElementById('cinemaModal').style.display = 'none';
		}
		
		function showTrailer(videoUrl) {
		    const modal = document.getElementById('trailerModal');
		    const videoSource = document.getElementById('videoSource');  // Get the <source> element
		    const videoElement = document.getElementById('trailerVideo'); // Get the video element
		    
		    if (!videoSource || !videoElement) {
		        console.error("The video element or source is not available.");
		        return;  // Exit early if the video element or source is not available
		    }

		    console.log("URL passed as parameter: " + videoUrl);  // Ensure this logs correctly

		    // Ensure the video source is reset every time the modal is opened
		    videoSource.setAttribute('src', videoUrl);

		    // Reload and reset the video
		    videoElement.load();  // Reload the video with the new source
		    videoElement.play();  // Optionally start playing the video

		    modal.style.display = 'block';  // Display the modal
		}

		function closeTrailerModal() {
		    const modal = document.getElementById('trailerModal');
		    const videoElement = document.getElementById('trailerVideo');

		    // Stop and reset the video when the modal is closed
		    videoElement.pause();
		    videoElement.currentTime = 0;

		    modal.style.display = 'none';  // Hide the modal
		}

		// Close modals when clicking outside
		window.onclick = function(event) {
			if (event.target.className === 'modal') {
				if (event.target.id === 'trailerModal') {
					closeTrailerModal();
				} else {
					event.target.style.display = 'none';
				}
			}
		}
	</script>

</body>
</html>
