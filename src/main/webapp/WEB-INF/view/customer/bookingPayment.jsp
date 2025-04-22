<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Booking Payment</title>
<link href="${pageContext.request.contextPath}/resources/css/movieFilter.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/seatBooking.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/bookSeats.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/resources/css/bookingPayment.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<style>
.payment-container {
	max-width: 1000px;
	margin: 1rem auto;
	padding: 1.5rem;
	background: rgba(33, 33, 33, 0.9);
	border-radius: 10px;
	border: 1px solid #dc3545;
	display: flex;
	flex-direction: column;
	gap: 1rem;
}

.payment-header {
	text-align: center;
	color: #dc3545;
}

.payment-summary {
	padding: 1rem;
	background: rgba(0, 0, 0, 0.2);
	border-radius: 5px;
	margin-bottom: 2rem;
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 0.5rem;
}

.summary-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 0.3rem;
	border-bottom: 1px solid rgba(220, 53, 69, 0.2);
}

.summary-item:last-child {
	border-bottom: none;
}

.summary-item span:first-child {
	color: #dc3545;
	font-weight: bold;
}

.summary-item span:last-child {
	color: #fff;
}
</style>
</head>
<body class="bg-dark">

	<jsp:include page="../cusSidebar.jsp"></jsp:include>

	<div class="main clearfix position-relative">
		<jsp:include page="../cusHeader.jsp"></jsp:include>

		<div class="payment-container">
			<div class="payment-header">
				<h2>
					<i class="fas fa-credit-card"></i> Payment Details
				</h2>
			</div>
			hello

			<form:form modelAttribute="sessionBookingObj"
				action="${pageContext.request.contextPath}/booking/processPayment"
				method="post" enctype="multipart/form-data">
				<form:hidden path="cusId" />
				<form:hidden path="mcId" />
				<form:hidden path="mcsId" />
				<form:hidden path="selectedShowtimeId" />
				<form:hidden path="totalAmount" />
				<form:hidden path="bookingDateStr" value="${bookingDate}" />
				<form:hidden path="bookedSeatIdList"/>

				<div class="payment-summary">
					<div class="summary-item">
						<span>Movie:</span> <span>${movieTitle}</span>
					</div>
					<div class="summary-item">
						<span>Cinema:</span> <span>${cinemaName}</span>
					</div>
					<div class="summary-item">
						<span>Showtime:</span> <span>${showtimeObj.getFormattedShowtime()}</span>
					</div>
					<div class="summary-item">
						<span>Seats:</span> <span> <c:forEach
								items="${sessionBookingObj.bookedSeatList}" var="seat"
								varStatus="status">
								${seat.seatNum}<c:if test="${!status.last}">, </c:if>
							</c:forEach> (${sessionBookingObj.bookedSeatIdList.size()} seats)
						</span>
					</div>
					<div class="summary-item">
						<span>ShowDate:</span> <span><fmt:formatDate
								value="${sessionBookingObj.bookingDate}"
								pattern="EEE MMM dd yyyy" /></span>
					</div>
					<div class="summary-item">
						<span>Total:</span> <span>$${sessionBookingObj.totalAmount}</span>
					</div>
				</div>

				<div class="payment-content">
					<div class="payment-left-section">
						<div class="payment-methods">
							<h4 class="text-light mb-3">Select Payment Method</h4>

							<form:select path="paymentMethodId" class="payment-method-select">
								<form:options items="${paymentList}" itemValue="id"
									itemLabel="method_name" />
							</form:select>
						</div>

						<c:forEach items="${paymentList}" var="payment">
							<div class="payment-details" id="payment-${payment.id}">
								<%-- <img src="${payment.qr_path}"
									alt="${payment.method_name} QR Code" class="payment-qr"> --%>
								<div class="payment-number">Account Number:
									${payment.acc_num}</div>
							</div>
						</c:forEach>
					</div>

					<div class="payment-right-section">
						<div class="receipt-upload">
							<h4 class="text-light mb-3">Upload Payment Receipt</h4>
							<label for="receipt">Please upload your payment receipt
								screenshot</label> <input type="file" id="receipt" name="receipt"
								accept="image/*" onchange="previewReceipt(this)">
							<img id="receipt-preview" class="receipt-preview"
								alt="Receipt preview">
						</div>

						<button type="submit" class="payment-btn">
							<i class="fas fa-lock"></i> Confirm Payment
						</button>
					</div>
				</div>
			</form:form>
		</div>

		<jsp:include page="../cusFooter.jsp"></jsp:include>
	</div>

	<script
		src="${pageContext.request.contextPath}/resources/js/bookingPayment.js"></script>

</body>
</html>
