<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Book Seats</title>
<link
	href="${pageContext.request.contextPath}/resources/css/movieFilter.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/css/seatBooking.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/css/bookSeats.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/css/seatAvailability.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>
<body class="bg-dark">

	<jsp:include page="../cusSidebar.jsp"></jsp:include>

	<div class="main clearfix position-relative">
		<jsp:include page="../cusHeader.jsp"></jsp:include>

		<!-- Toast notification container -->
		<div id="toastNotification" class="toast-notification"></div>

		<div class="container-fluid py-3">
			<form:form class="row g-2" modelAttribute="sessionBookingObj"
				id="seatBookingForm"
				action="${pageContext.request.contextPath}/booking/submitBooking"
				method="post" onsubmit="return confirmBooking(event)">

				<form:hidden path="mcId" value="${mcsObj.id}" id="mcId" />
				<input type="hidden" name="movieTitle"
					value="${mcsObj.movieObj.title}">
				<input type="hidden" name="cinemaName"
					value="${mcsObj.cinemaObj.name}">
				<input type="hidden" name="contextPath" id="contextPath"
					value="${pageContext.request.contextPath}">

				<!-- Top navigation row with back button, showtimes, and date -->
				<div class="row mb-3">
					<div class="col-12">
						<div class="d-flex flex-wrap align-items-center">
							<!-- Back button -->
							<a href="javascript:history.back()" class="back-button"> <i
								class="fas fa-arrow-left"></i> Back
							</a>

							<!-- Showtime selection -->
							<div class="showtime-selection ms-3">
								<div class="showtime-title">
									<i class="far fa-clock"></i> Showtimes:
								</div>
								<div class="showtime-options">
									<c:forEach var="showtime" items="${mcsObj.showtimeList}"
										varStatus="status">
										<div class="showtime-option">
											<input type="radio" name="selectedShowtimeId"
												id="time${showtime.id}" value="${showtime.id}"
												${empty selectedShowtimeId && status.first ? 'checked' : ''}
												data-formatted-showtime="${showtime.getFormattedShowtime()}"
												onchange="handleSelectionChange()" /> <label
												for="time${showtime.id}">${showtime.getFormattedShowtime()}</label>
										</div>
									</c:forEach>

									<!-- Hidden input to store the selected showtime ID -->
									<input type="hidden" id="selectedShowtimeId" value="">

								</div>
							</div>

							<!-- Date selection -->
							<div class="date-selection ms-auto">
								<div class="date-title">Select a Date:</div>
								<select class="date-input form-control" id="dateInput"
									name="selectedDate" onchange="handleSelectionChange()">
									<!-- Loop through available airing dates and create an option for each -->
									<c:forEach var="airingDate"
										items="${mcsObj.getAvailableDates()}">
										<option value="${airingDate}"
											${airingDate == selectedDate ? 'selected' : ''}>
											${airingDate}</option>
									</c:forEach>
								</select>
							</div>

						</div>
					</div>
				</div>

				<!-- Main content row with seats and summary -->
				<div class="row">
					<div class="col-md-8">
						<div class="seat-layout-card">
							<div class="seat-layout-header">
								<h1 class="seat-layout-title">
									<i class="fas fa-chair"></i> Seat Selection
								</h1>
							</div>

							<div class="screen-container">
								<div class="screen">
									<p class="screen-label text-center">Cinema Screen</p>
								</div>
							</div>

							<div class="seat-container">
								<c:forEach var="row" begin="1"
									end="${mcsObj.cinemaObj.rowCount}">
									<div class="seat-row">
										<span class="row-label">Row ${row}</span>

										<c:forEach var="seat" items="${mcsObj.cinemaObj.seatList}">
											<c:if test="${seat.row == row}">
												<label
													class="seat ${seat.seatTypeName.toLowerCase()} 
											        ${seat.status == 0 ? 'unavailable' : ''} 
											        ${sessionBookingObj.bookedSeatIdList.contains(seat.id) ? 'selected' : ''}"
													style="width: calc(${seat.seatSize} * 30px);"
													data-seat-id="${seat.id}"
													data-seat-type-name="${seat.seatTypeName}"
													data-seat-num="${seat.seatNum}"
													data-seat-price="${seat.price}"> <form:checkbox
														path="bookedSeatIdList" value="${seat.id}"
														disabled="${seat.status == 0}" /> <i
													class="${seat.seatTypeName == 'Couple' || seat.seatTypeName == 'VIP-Couple' ? 'fas fa-couch' : 'fas fa-chair'}"></i>
													<span class="seat-number">${seat.seatNum}</span>
													<div class="seat-tooltip">${seat.seatTypeName}-${seat.seatNum}
														- $${seat.price} ${seat.status == 0 ? '- Not Available' : ''}</div>
												</label>
											</c:if>
										</c:forEach>

									</div>
								</c:forEach>
							</div>

							<div class="legend-container">
								<div class="d-flex justify-content-between gap-2">
									<div class="d-flex align-items-center">
										<div class="legend-color me-2"
											style="background: linear-gradient(145deg, #2d2d2d, #1a1a1a);"></div>
										<span class="text-white">Single Seat</span>
									</div>
									<div class="d-flex align-items-center">
										<div class="legend-color me-2"
											style="background: linear-gradient(145deg, #2d2d2d, #1a1a1a); border: 2px solid #0dcaf0;"></div>
										<span class="text-white">Couple Seat</span>
									</div>
									<div class="d-flex align-items-center">
										<div class="legend-color me-2"
											style="background: linear-gradient(145deg, #2d2d2d, #1a1a1a); border: 2px solid #b88a44;"></div>
										<span class="text-white">VIP Single</span>
									</div>
									<div class="d-flex align-items-center">
										<div class="legend-color me-2"
											style="background: linear-gradient(145deg, #2d2d2d, #1a1a1a); border: 2px solid #d4af37;"></div>
										<span class="text-white">VIP Couple</span>
									</div>
									<div class="d-flex align-items-center">
										<div class="legend-color me-2"
											style="background: linear-gradient(145deg, #2d2d2d, #1a1a1a); position: relative;">
											<div
												style="position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: repeating-linear-gradient(45deg, rgba(0, 0, 0, 0.3), rgba(0, 0, 0, 0.3) 5px, rgba(0, 0, 0, 0.4) 5px, rgba(0, 0, 0, 0.4) 10px); border-radius: 8px;"></div>
										</div>
										<span class="text-white">Not Available</span>
									</div>
									<div class="d-flex align-items-center">
										<div class="legend-color me-2"
											style="background: linear-gradient(145deg, #222222, #1a1a1a); position: relative;">
											<div
												style="position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); color: rgba(220, 53, 69, 0.5); font-size: 1.5em;">×</div>
										</div>
										<span class="text-white">Occupied</span>
									</div>
								</div>
							</div>
						</div>
					</div>

					<div class="col-md-4">
						<div class="summary-booking">
							<div class="summary-header">
								<h1 class="summary-title">
									<i class="fas fa-ticket-alt"></i> Booking Summary
								</h1>
							</div>

							<form:hidden path="cusId" value="${sessionScope.accountInfo.id}" />

							<div class="summary-detail-section">
								<div class="summary-detail-content">
									<p>
										<i class="fas fa-film"></i><strong>${mcsObj.movieObj.title}</strong>
									</p>
									<p>
										<i class="fas fa-building"></i> ${mcsObj.cinemaObj.name}
									</p>
									<p>
										<i class="far fa-calendar"></i> <span id="summary-bookDate">---</span>
										| <span id="summary-bookShowtime">---</span>
									</p>
									<div class="summary-seats-info">
										<h4 class="mb-3">Selected Seats</h4>
										<div id="selectedSeatsContainer">
											<!-- Selected seats will be dynamically populated here -->
											<p class="text-danger">No seats selected</p>
										</div>
									</div>
								</div>
							</div>

							<div class="summary-total-price">
								<span>Total</span> <span>$0.00</span>
							</div>

							<button type="submit" class="summary-confirm-btn">
								<i class="fas fa-check-circle"></i> Confirm Booking
							</button>
						</div>
					</div>
				</div>

			</form:form>
		</div>

		<jsp:include page="../cusFooter.jsp"></jsp:include>
	</div>

	<script
		src="${pageContext.request.contextPath}/resources/js/seatAvailability.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/confirmBooking.js"></script>

</body>
</html>

