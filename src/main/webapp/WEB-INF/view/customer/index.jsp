<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cinema Booking</title>

<style type="text/css">
/* Header Banner */
.cinema-banner {
	background: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.9)),
		url('resources/images/WAZIYA.jpg');
	background-size: cover;
	padding: 60px 0;
	text-align: center;
}

.banner-content h1 {
	font-size: 3.5rem;
	margin-bottom: 20px;
}

.banner-content p {
	font-size: 1.2rem;
	color: var(--gray);
	margin-bottom: 30px;
}

/* Pagination styles */
.pagination {
	display: flex;
	justify-content: center;
	margin-top: 30px;
}

.pagination-btn {
	margin: 0 5px;
	padding: 8px 15px;
	border: none;
	background-color: #2a2a2a;
	color: #fff;
	cursor: pointer;
	border-radius: 4px;
	transition: all 0.3s ease;
	font-weight: 500;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.pagination-btn.active {
	background-color: #8b0000;
	color: white;
	transform: translateY(-2px);
	box-shadow: 0 4px 8px rgba(139, 0, 0, 0.3);
}

.pagination-btn:hover:not(.active) {
	background-color: #3a3a3a;
	transform: translateY(-1px);
}

.pagination-btn:active {
	transform: translateY(0);
}

.cinema-card {
	display: none;
	transition: opacity 0.3s ease;
}

.cinema-card.active {
	display: block;
	animation: fadeIn 0.5s;
}

@
keyframes fadeIn {from { opacity:0;
	
}

to {
	opacity: 1;
}
}
</style>

</head>
<body>

	<jsp:include page="../cusSidebar.jsp"></jsp:include>

	<div class="main clearfix position-relative">

		<jsp:include page="../cusHeader.jsp"></jsp:include>

		<!-- Cinema Banner -->
		<div class="cinema-banner">
			<div class="container">
				<div class="banner-content">
					<h1>Experience Movies Like Never Before</h1>
					<p>Book your perfect movie experience with premium comfort and
						state-of-the-art technology</p>
				</div>
			</div>
		</div>

		<!-- Quick Booking Widget -->
		<div class="container">
			<div class="booking-widget">
				<h3 class="widget-title">Quick Movie Booking</h3>

				<form:form modelAttribute="mcsObj" class="booking-form"
					action="${pageContext.request.contextPath}/showFilteredMovieShowtimes"
					method="get">

					<div class="form-group">
						<label class="form-label">Select Cinema</label>
						<form:select path="cinemaId" class="form-select">
							<form:option value="">Choose a cinema</form:option>
							<form:options items="${cinemaList}" itemValue="id"
								itemLabel="name" />
						</form:select>
					</div>

					<div class="form-group">
						<label class="form-label">Select Movie</label>
						<form:select path="movieId" class="form-select">
							<form:option value="">Choose a movie</form:option>
							<form:options items="${movieList}" itemValue="id"
								itemLabel="title" />
						</form:select>
					</div>

					<div class="form-group">
						<label class="form-label">Select Date</label>
						<div class="input-group date">
							<input type="text" name="selectedDate"
								class="form-control datepicker" placeholder="DD/MM/YYYY" /> <span
								class="input-group-text"><i class="fas fa-calendar-alt"></i></span>
						</div>
					</div>

					<div class="col d-none">
						<select class="form-select" name="selectedShowtimeId">
							<option value="">Select a showtime</option>
							<c:forEach items="${showtimeList}" var="showtime">
								<option value="${showtime.id}">${showtime.startHour}</option>
							</c:forEach>
						</select>
					</div>

					<div>
						<button type="submit" class="search-btn">
							<i class="fas fa-search"></i> Find Available Seats
						</button>
					</div>
				</form:form>

			</div>
		</div>

		<!-- Cinema Locations Section -->
		<section class="cinema-section">
			<div class="container">

				<div class="section-title">
					<h2>Our Cinema Locations</h2>
					<span class="badge">${cinemaList.size()} Locations</span>
					<!-- Dynamically show the number of locations -->
				</div>
				<div class="cinema-grid" id="cinemaGrid">
					<c:forEach var="cinema" items="${cinemaList}" varStatus="status">
						<div class="cinema-card" id="cinema-${status.index}">
							<div class="cinema-image">
								<img src="${cinema.uploadedImgs[0].imgUrl}" alt="${cinema.name}">
							</div>
							<div class="cinema-info">
								<h3 class="cinema-name">${cinema.name}</h3>
								<p class="cinema-details">${cinema.townshipName},
									${cinema.stateName}</p>
								<div class="cinema-features">
									<!-- Looping through selected features list -->
									<c:forEach var="feature" items="${cinema.selectedFeatureList}">
										<span class="feature-badge">${feature.name}</span>
										<!-- Assuming FeatureBean has a 'name' property -->
									</c:forEach>
								</div>
								<a
									href="${pageContext.request.contextPath}/showFilteredMovieShowtimes?selectedDate=&selectedShowtimeId=&cinemaId=${cinema.id}&movieId="
									class="book-btn">View Showtimes</a>
							</div>
						</div>
					</c:forEach>
				</div>

				<!-- Pagination controls -->
				<div class="pagination" id="cinemaPagination"></div>

			</div>
		</section>

		<!-- Now Showing Section -->
		<section class="movie-section">
			<div class="container">
				<div class="section-title">
					<h2>Now Showing</h2>
					<span class="badge">${movieList.size()} Movies</span>
					<!-- Dynamically show the number of movies -->
				</div>

				<div class="featured-movies">
					<c:forEach var="movie" items="${movieList}">
						<div class="movie-card">
							<div class="movie-poster">
								<img src="${movie.uploadedImgs[0].imgUrl}" alt="Dune: Part Two">
								<div class="movie-overlay">
									<div class="movie-details">
										<h2>${movie.title}</h2>
										<div class="movie-meta">
											<span><i class="fas fa-clock"></i>
												${movie.getFormattedDuration()}</span> <span><i
												class="fas fa-ticket-alt"></i> Pre-booking</span>
										</div>
										<p class="synopsis">${movie.getShortDescription()}
										</p>
										<a href="${pageContext.request.contextPath}/showFilteredMovieShowtimes?selectedDate=&selectedShowtimeId=&cinemaId=&movieId=${movie.id}" 
										class="ticket-btn">Pre-book Now <i
											class="fas fa-arrow-right"></i></a>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>


				<!-- <div class="section-title">
					<h2>Now Showing</h2>
					<span class="badge">12 Movies</span>
				</div>

				<div class="featured-movies">
					<div class="movie-card">
						<div class="movie-poster">
							<img src="resources/cusTemplate/img/DunePoster.jpg"
								alt="Dune: Part Two">
							<div class="movie-overlay">
								<div class="movie-details">
									<h2>Dune: Part Two</h2>
									<div class="movie-meta">
										<span><i class="fas fa-clock"></i> 2h 45m</span> <span><i
											class="fas fa-ticket-alt"></i> Pre-booking</span>
									</div>
									<p class="synopsis">Continue the epic journey through
										Arrakis in this stunning sequel.</p>
									<a href="#" class="ticket-btn">Pre-book Now <i
										class="fas fa-arrow-right"></i></a>
								</div>
							</div>
						</div>
					</div>
				</div> -->
			</div>
		</section>

		<jsp:include page="../cusFooter.jsp"></jsp:include>
	</div>

	<script>
		document.addEventListener('DOMContentLoaded', function() {
			// Cinema pagination
			const itemsPerPage = 3;
			const cinemaCards = document.querySelectorAll('.cinema-card');
			const totalPages = Math.ceil(cinemaCards.length / itemsPerPage);
			const paginationContainer = document.getElementById('cinemaPagination');
			
			// Create pagination buttons
			for (let i = 1; i <= totalPages; i++) {
				const pageBtn = document.createElement('button');
				pageBtn.classList.add('pagination-btn');
				pageBtn.textContent = i;
				pageBtn.addEventListener('click', function() {
					showPage(i);
					
					// Update active button
					document.querySelectorAll('.pagination-btn').forEach(btn => {
						btn.classList.remove('active');
					});
					this.classList.add('active');
				});
				paginationContainer.appendChild(pageBtn);
			}
			
			// Show first page by default
			if (document.querySelector('.pagination-btn')) {
				document.querySelector('.pagination-btn').classList.add('active');
				showPage(1);
			}
			
			// Function to show specific page
			function showPage(pageNum) {
				const startIndex = (pageNum - 1) * itemsPerPage;
				const endIndex = startIndex + itemsPerPage;
				
				cinemaCards.forEach((card, index) => {
					if (index >= startIndex && index < endIndex) {
						card.classList.add('active');
					} else {
						card.classList.remove('active');
					}
				});
			}
		});
	</script>
</body>
</html>
