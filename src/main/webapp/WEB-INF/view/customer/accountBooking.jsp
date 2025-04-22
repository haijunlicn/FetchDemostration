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
<title>My Bookings</title>
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
body {
	background: #0a0a0a;
	font-family: 'Inter', sans-serif;
	margin: 0;
	padding: 20px;
}

.booking-container {
	max-width: 1200px;
	margin: 0 auto;
	padding: 30px;
}

.booking-title {
	font-size: 24px;
	font-weight: 600;
	color: #ffffff;
	margin-bottom: 30px;
}

.booking-list {
	display: grid;
	gap: 20px;
}

.booking-item {
	background: rgba(20, 20, 20, 0.95);
	border-radius: 10px;
	padding: 20px;
	box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
	transition: all 0.3s ease;
	border: 1px solid rgba(255, 255, 255, 0.1);
	cursor: pointer;
}

.booking-item:hover {
	transform: translateY(-2px);
	box-shadow: 0 6px 8px rgba(0, 0, 0, 0.3);
}

.booking-details {
	display: grid;
	grid-template-columns: 1fr auto auto;
	gap: 20px;
	align-items: center;
}

.booking-summary {
	display: flex;
	align-items: center;
	gap: 20px;
}

.booking-expanded {
	display: none;
	grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
	gap: 20px;
	margin-top: 20px;
	padding-top: 20px;
	border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.booking-item.expanded .booking-expanded {
	display: grid;
}

.toggle-details {
	color: #ffffff;
	background: none;
	border: none;
	cursor: pointer;
	padding: 8px;
	transition: transform 0.3s ease;
}

.booking-item.expanded .toggle-details {
	transform: rotate(180deg);
}

.detail-label {
	font-size: 13px;
	color: #a1a1aa;
	margin-bottom: 4px;
}

.detail-value {
	font-size: 15px;
	font-weight: 500;
	color: #ffffff;
}

.seat-list {
	font-size: 14px;
	color: #d4d4d8;
	margin-top: 4px;
	display: flex;
	flex-wrap: wrap;
	gap: 6px;
}

.seat-tag {
	background: rgba(255, 255, 255, 0.1);
	padding: 2px 8px;
	border-radius: 4px;
	font-size: 12px;
}

.booking-status {
	display: inline-flex;
	align-items: center;
	padding: 6px 12px;
	border-radius: 9999px;
	font-size: 12px;
	font-weight: 500;
	white-space: nowrap;
}

.status-pending {
	background-color: #422006;
	color: #fbbf24;
}

.status-confirmed {
	background-color: #064e3b;
	color: #34d399;
}

.status-declined {
	background-color: #450a0a;
	color: #f87171;
}

.status-cancelled {
	background-color: #3f0f1d;
	color: #fb7185;
}

.booking-status::before {
	content: '';
	width: 6px;
	height: 6px;
	border-radius: 50%;
	margin-right: 6px;
}

.status-pending::before {
	background-color: #fbbf24;
}

.status-confirmed::before {
	background-color: #34d399;
}

.status-declined::before {
	background-color: #f87171;
}

.status-cancelled::before {
	background-color: #fb7185;
}

.payment-details {
	margin-top: 15px;
	border-top: 1px solid rgba(255, 255, 255, 0.1);
	padding-top: 15px;
}

.receipt-image {
	max-width: 100%;
	max-height: 80px;
	border-radius: 6px;
	margin-top: 8px;
}

.pagination {
	display: flex;
	justify-content: center;
	margin-top: 30px;
	gap: 10px;
}

.pagination-button {
	background: rgba(40, 40, 40, 0.8);
	color: #ffffff;
	border: 1px solid rgba(255, 255, 255, 0.2);
	border-radius: 6px;
	padding: 8px 16px;
	cursor: pointer;
	transition: all 0.2s ease;
}

.pagination-button:hover {
	background: rgba(60, 60, 60, 0.8);
}

.pagination-button.active {
	background: rgba(80, 80, 80, 0.9);
	border-color: rgba(255, 255, 255, 0.4);
}

.pagination-button:disabled {
	opacity: 0.5;
	cursor: not-allowed;
}

.pagination-info {
	color: #a1a1aa;
	font-size: 14px;
	margin-top: 10px;
	text-align: center;
}
</style>
</head>
<body>
	<jsp:include page="../cusSidebar.jsp"></jsp:include>

	<div class="main clearfix position-relative">
		<jsp:include page="../cusHeader.jsp"></jsp:include>

		<div class="booking-container">
			<h2 class="booking-title">My Bookings</h2>
			<div class="booking-list" id="bookingList">
				<c:forEach items="${bookingList}" var="booking">
					<div class="booking-item"
						onclick="this.classList.toggle('expanded')">
						<div class="booking-details">
							<div class="booking-summary">
								<div class="detail-value">#${booking.id} -
									${booking.movieTitle}</div>
								<div class="detail-value">|</div>
								<div class="detail-value">${booking.cinemaName}</div>
								<div class="detail-value">|</div>
								<div class="detail-value">
									<fmt:parseDate value="${booking.bookingDate}"
										pattern="yyyy-MM-dd" var="parsedDate" />
									<fmt:formatDate value="${parsedDate}" pattern="dd MMM yyyy" />
									at ${booking.selectedShowtimeObj.getFormattedShowtime()}
								</div>
								<div class="detail-value">|</div>
								<div class="seat-list">
									<c:forEach items="${booking.bookedSeatList}" var="seat"
										varStatus="status">
										<span class="seat-tag">${seat.seatNum} ($${seat.price})</span>
									</c:forEach>
								</div>
							</div>

							<div
								class="booking-status 
								${booking.status == 0 ? 'status-declined' : 
								booking.status == 1 ? 'status-pending' : 
								booking.status == 2 ? 'status-confirmed' : 
								'status-cancelled'}">
								${booking.status == 0 ? 'Declined' : 
								booking.status == 1 ? 'Pending' : 
								booking.status == 2 ? 'Confirmed' : 
								'Cancelled'}
							</div>

							<button class="toggle-details">
								<i class="fas fa-chevron-down"></i>
							</button>
						</div>

						<div class="booking-expanded">
							<div class="detail-group">
								<div class="detail-label">Payment Method</div>
								<div class="detail-value">${booking.paymentMethodName}</div>
							</div>
							<div class="detail-group">
								<div class="detail-label">Total Amount</div>
								<div class="detail-value">$${booking.totalAmount}</div>
							</div>
							<div class="detail-group">
								<div class="detail-label">Transaction Date</div>
								<div class="detail-value">
									<c:if test="${booking.createdDate != null}">
										<fmt:formatDate value="${booking.createdDate}"
											pattern="dd MMM yyyy, HH:mm:ss" />
									</c:if>
									<c:if test="${booking.createdDate == null}">
										Pending
									</c:if>
								</div>
							</div>
							<c:if test="${booking.screenshotPath != null}">
								<div class="detail-group">
									<div class="detail-label">Receipt</div>
									<img src="${booking.screenshotPath}" alt="Receipt"
										class="receipt-image">
								</div>
							</c:if>
						</div>
					</div>
				</c:forEach>
			</div>
			
			<div class="pagination" id="pagination"></div>
			<div class="pagination-info" id="paginationInfo"></div>
		</div>

		<jsp:include page="../cusFooter.jsp"></jsp:include>
	</div>

<script>
document.addEventListener('DOMContentLoaded', function() {
    // Pagination configuration
    const itemsPerPage = 5; // Changed from 10 to 5 bookings per page
    const bookingItems = document.querySelectorAll('.booking-item');
    const totalItems = bookingItems.length;
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    let currentPage = 1;
    
    // Initialize pagination
    setupPagination();
    showPage(currentPage);
    
    function setupPagination() {
        const paginationContainer = document.getElementById('pagination');
        paginationContainer.innerHTML = '';
        
        // Previous button
        const prevButton = document.createElement('button');
        prevButton.classList.add('pagination-button');
        prevButton.innerHTML = '<i class="fas fa-chevron-left"></i>';
        prevButton.addEventListener('click', function() {
            if (currentPage > 1) {
                currentPage--;
                showPage(currentPage);
                updatePaginationButtons();
            }
        });
        paginationContainer.appendChild(prevButton);
        
        // Page buttons
        for (let i = 1; i <= totalPages; i++) {
            const pageButton = document.createElement('button');
            pageButton.classList.add('pagination-button');
            pageButton.textContent = i;
            pageButton.addEventListener('click', function() {
                currentPage = i;
                showPage(currentPage);
                updatePaginationButtons();
            });
            paginationContainer.appendChild(pageButton);
        }
        
        // Next button
        const nextButton = document.createElement('button');
        nextButton.classList.add('pagination-button');
        nextButton.innerHTML = '<i class="fas fa-chevron-right"></i>';
        nextButton.addEventListener('click', function() {
            if (currentPage < totalPages) {
                currentPage++;
                showPage(currentPage);
                updatePaginationButtons();
            }
        });
        paginationContainer.appendChild(nextButton);
        
        updatePaginationButtons();
    }
    
    function showPage(page) {
        const startIndex = (page - 1) * itemsPerPage;
        const endIndex = Math.min(startIndex + itemsPerPage - 1, totalItems - 1);
        
        // Hide all items
        bookingItems.forEach(item => {
            item.style.display = 'none';
        });
        
        // Show items for current page
        for (let i = startIndex; i <= endIndex; i++) {
            if (bookingItems[i]) {
                bookingItems[i].style.display = 'block';
            }
        }
        
        // Update pagination info
        updatePaginationInfo(startIndex, endIndex);
    }
    
    function updatePaginationButtons() {
        const buttons = document.querySelectorAll('.pagination-button');
        
        buttons.forEach((button, index) => {
            // First button is previous, last is next
            if (index === 0) {
                button.disabled = currentPage === 1;
            } else if (index === buttons.length - 1) {
                button.disabled = currentPage === totalPages;
            } else {
                // Page number buttons
                const pageNum = index;
                button.classList.toggle('active', pageNum === currentPage);
            }
        });
    }
    
    function updatePaginationInfo(startIndex, endIndex) {
        const infoElement = document.getElementById('paginationInfo');
        if (totalItems === 0) {
            infoElement.textContent = 'No bookings found';
        } else {
            infoElement.textContent = "Showing " + (startIndex + 1) + " to " + (endIndex + 1) + " of " + totalItems + " bookings";
        }
    }
});
</script>
</body>
</html>
