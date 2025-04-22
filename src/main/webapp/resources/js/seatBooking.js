/**
 * 
 */

document.addEventListener('DOMContentLoaded', function() {
	handleSelectionChange();

	// Seat selection logic
	document.querySelectorAll('.seat:not(.occupied):not(.unavailable)').forEach(seat => {
		seat.addEventListener('click', () => {
			seat.classList.toggle('selected');
		});
	});

});

// Function to show toast notification
function showToast(message, isSuccess) {
	const toast = document.getElementById('toastNotification');

	toast.textContent = message;

	// Ensure base class is present
	toast.classList.add('toast-notification');

	// Remove both classes first to avoid conflicts
	toast.classList.remove('toast-success', 'toast-error');

	// Add appropriate class based on success/failure
	if (isSuccess) {
		toast.classList.add('toast-success');
	} else {
		toast.classList.add('toast-error');
	}

	toast.style.display = 'block';

	// Add 'show' class after a brief delay to trigger transition
	setTimeout(() => {
		toast.classList.add('show');
	}, 10);

	// Hide the toast after 3 seconds
	setTimeout(() => {
		toast.classList.remove('show');
		setTimeout(() => {
			toast.style.display = 'none';
		}, 300);
	}, 3000);
}

// Attach event listeners to all seat checkboxes
document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
	checkbox.addEventListener('change', function() {
		const seat = this.closest('.seat');
		const seatId = seat.dataset.seatId;
		const showtimeId = document.querySelector('input[name="selectedShowtimeId"]:checked')?.value;
		const bookingDate = document.getElementById('dateInput')?.value;
		const mcId = document.getElementById('mcId')?.value;

		if (!showtimeId || !bookingDate || !mcId) {
			showToast("No showtime, booking date, or mcId selected!", false);
			console.error("No showtime, booking date, or mcId selected!");
			return;
		}

		updateSeatSelection(seatId, this.checked, showtimeId, bookingDate, mcId);
	});
});

function logSelectedSeats() {
	// Get all selected seat elements
	const selectedSeats = document.querySelectorAll('.seat.selected');
	const selectedSeatsContainer = document.getElementById('selectedSeatsContainer');
	let totalPrice = 0;

	// Clear the container
	selectedSeatsContainer.innerHTML = '';

	// Check if no selected seats are found
	if (selectedSeats.length === 0) {
		selectedSeatsContainer.innerHTML = '<p class="text-danger">No seats selected</p>';
		document.querySelector('.summary-total-price span:last-child').textContent = '$0.00';
		return;
	}

	// Create a map to group seats by type
	const seatsByType = new Map();

	// Group seats by their type
	selectedSeats.forEach(seat => {
		const seatType = seat.dataset.seatTypeName;
		const seatNum = seat.dataset.seatNum;
		const seatPrice = parseFloat(seat.dataset.seatPrice);

		if (!seatsByType.has(seatType)) {
			seatsByType.set(seatType, {
				seats: [],
				totalPrice: 0
			});
		}

		seatsByType.get(seatType).seats.push(seatNum);
		seatsByType.get(seatType).totalPrice += seatPrice;
		totalPrice += seatPrice;
	});

	// Create HTML for each seat type group
	seatsByType.forEach(function(data, type) {
		const typeDiv = document.createElement('div');
		typeDiv.className = 'seat-type-group mb-2';
		typeDiv.innerHTML =
			'<div class="d-flex justify-content-between align-items-center">' +
			'<strong>' + type + '</strong>' +
			'<span>$' + data.totalPrice.toFixed(2) + '</span>' +
			'</div>' +
			'<div class="seat-numbers">' + data.seats.join(', ') + '</div>';
		selectedSeatsContainer.appendChild(typeDiv);
	});

	// Update total price
	document.querySelector('.summary-total-price span:last-child').textContent = '$' + totalPrice.toFixed(2);
}

// Function to update seat selection and refresh checkboxes if needed
function updateSeatSelection(seatId, isChecked, showtimeId, bookingDate, mcId) {
	// Get the formatted showtime from the selected radio input
	const showtimeElement = document.getElementById('time' + showtimeId);
	const formattedShowtime = showtimeElement ? showtimeElement.dataset.formattedShowtime : "Unknown Showtime"; // Use the dataset attribute

	fetch(`${window.location.origin}${pageContext.request.contextPath}/updateBookingSeat`, {
		method: 'POST',
		headers: { 'Content-Type': 'application/json' },
		body: JSON.stringify({ seatId, isChecked, showtimeId, bookingDate, mcId })
	})
		.then(response => response.json())
		.then(data => {
			console.log("Seat updated:", data);
			if (data.success) {
				console.log("Seat " + seatId + " has been " + (isChecked ? "booked" : "unbooked") +
					" for date " + bookingDate + ", Showtime " + formattedShowtime + ".");
				document.getElementById('summary-bookDate').textContent = bookingDate;
				document.getElementById('summary-bookShowtime').textContent = formattedShowtime;
				refreshSeatSelection(data.bookedSeatIdList, data.occupiedSeatIdList);
				showToast(data.message, true);
			} else {
				showToast(data.message, false);
			}
		})
		.catch(error => {
			console.error('Error updating seat selection:', error);
			showToast('An error occurred while updating the seat selection', false);
		});
}

// Function to refresh checkboxes based on updated bookedSeatIdList
function refreshSeatSelection(bookedSeatIdList, occupiedSeatIdList) {
	document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
		const seat = checkbox.closest('.seat');
		const seatId = parseInt(seat.getAttribute('data-seat-id'));

		if (occupiedSeatIdList.includes(seatId)) {
			checkbox.checked = true;
			checkbox.disabled = true; // Disable selection for occupied seats
			seat.classList.add('occupied');
			seat.classList.remove('selected'); // Remove 'selected' if occupied
		} else if (bookedSeatIdList.includes(seatId)) {
			checkbox.checked = true;
			seat.classList.add('selected');
			seat.classList.remove('occupied'); // Ensure it's not marked as occupied
		} else {
			checkbox.checked = false;
			checkbox.disabled = false; // Enable selection if not occupied
			seat.classList.remove('selected', 'occupied');
		}
	});

	// Call function to log all selected seats
	logSelectedSeats();
}

function handleSelectionChange() {
	// Get the selected showtimeId (radio button) value
	const showtimeId = document.querySelector('input[name="selectedShowtimeId"]:checked')?.value;
	// Get the value of bookingDate (date input)
	const bookingDate = document.getElementById('dateInput')?.value;
	// Get mcId value
	const mcId = document.getElementById('mcId')?.value;

	console.log("It changes!");

	if (!showtimeId || !bookingDate || !mcId) {
		console.error("Missing required values!");
		return;
	}

	updateSeatSelection(null, true, showtimeId, bookingDate, mcId);
}