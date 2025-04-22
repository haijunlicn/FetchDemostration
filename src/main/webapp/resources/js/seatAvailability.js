handleSelectionChange();

// Function to handle selection changes (date or showtime)
function handleSelectionChange() {
	// Get the selected values
	const showtimeId = document.querySelector('input[name="selectedShowtimeId"]:checked')?.value
	const bookingDate = document.getElementById("dateInput")?.value
	const mcId = document.getElementById("mcId")?.value

	if (!showtimeId || !bookingDate || !mcId) {
		console.error("Missing required values for seat availability update")
		return
	}

	fetchSeatAvailability(showtimeId, bookingDate, mcId)
}

// Function to fetch seat availability from the server
function fetchSeatAvailability(showtimeId, bookingDate, mcId) {
	// Get the formatted showtime for display
	const showtimeElement = document.getElementById("time" + showtimeId)
	const formattedShowtime = showtimeElement ? showtimeElement.dataset.formattedShowtime : "Unknown Showtime"

	// Update summary display
	document.getElementById("summary-bookDate").textContent = bookingDate
	document.getElementById("summary-bookShowtime").textContent = formattedShowtime

	const contextPath = document.getElementById("contextPath").value;

	// Fetch data from server
	fetch(`${window.location.origin}${contextPath}/updateBookingSeat`, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ showtimeId, bookingDate, mcId }),
	})
		.then((response) => {
			if (!response.ok) {
				throw new Error("Network response was not ok")
			}
			return response.json()
		})
		.then((data) => {
			if (data.success) {
				// Update seat availability in the UI
				updateSeatAvailability(data.occupiedSeatIdList)
				showToast("Seat availability updated", true)
			} else {
				showToast(data.message || "Failed to update seat availability", false)
			}
		})
		.catch((error) => {
			console.error("Error fetching seat availability:", error)
			showToast("Failed to fetch seat availability", false)
		})
		.finally(() => {
			// Remove loading state
			document.querySelectorAll(".seat").forEach((seat) => {
				seat.classList.remove("loading")
			})
		})
}

// Function to update seat availability in the UI
function updateSeatAvailability(occupiedSeatIdList) {
	// Reset all seats first (except unavailable ones)
	document.querySelectorAll(".seat:not(.unavailable)").forEach((seat) => {
		const seatId = Number.parseInt(seat.getAttribute("data-seat-id"))
		const checkbox = seat.querySelector('input[type="checkbox"]')

		// Reset selection state
		seat.classList.remove("occupied", "selected")
		checkbox.checked = false
		checkbox.disabled = false

		// Mark occupied seats
		if (occupiedSeatIdList.includes(seatId)) {
			seat.classList.add("occupied")
			checkbox.disabled = true
		}
	})

	// Update the booking summary
	updateBookingSummary()
}

// Function to update the booking summary
function updateBookingSummary() {
	const selectedSeats = document.querySelectorAll(".seat.selected")
	const selectedSeatsContainer = document.getElementById("selectedSeatsContainer")
	let totalPrice = 0

	selectedSeatsContainer.innerHTML = ""

	// Check if no selected seats are found
	if (selectedSeats.length === 0) {
		selectedSeatsContainer.innerHTML = '<p class="text-danger">No seats selected</p>'
		document.querySelector(".summary-total-price span:last-child").textContent = "$0.00"
		return
	}

	// Group seats by their type
	const seatsByType = new Map()
	selectedSeats.forEach((seat) => {
		const seatType = seat.dataset.seatTypeName
		const seatNum = seat.dataset.seatNum
		const seatPrice = Number.parseFloat(seat.dataset.seatPrice)

		if (!seatsByType.has(seatType)) {
			seatsByType.set(seatType, {
				seats: [],
				totalPrice: 0,
			})
		}

		seatsByType.get(seatType).seats.push(seatNum)
		seatsByType.get(seatType).totalPrice += seatPrice
		totalPrice += seatPrice
	})

	// Create HTML for each seat type group
	seatsByType.forEach((data, type) => {
		const typeDiv = document.createElement("div")
		typeDiv.className = "seat-type-group mb-2"
		typeDiv.innerHTML =
			'<div class="d-flex justify-content-between align-items-center">' +
			"<strong>" +
			type +
			"</strong>" +
			"<span>$" +
			data.totalPrice.toFixed(2) +
			"</span>" +
			"</div>" +
			'<div class="seat-numbers">' +
			data.seats.join(", ") +
			"</div>"
		selectedSeatsContainer.appendChild(typeDiv)
	})

	// Update total price
	document.querySelector(".summary-total-price span:last-child").textContent = "$" + totalPrice.toFixed(2)
}

// Function to show toast notification
function showToast(message, isSuccess) {
	const toast = document.getElementById("toastNotification")
	toast.textContent = message
	toast.classList.add("toast-notification")

	// Remove both classes first to avoid conflicts
	toast.classList.remove("toast-success", "toast-error")

	// Add appropriate class based on success/failure
	if (isSuccess) {
		toast.classList.add("toast-success")
	} else {
		toast.classList.add("toast-error")
	}

	toast.style.display = "block"
	
	setTimeout(() => {
		toast.classList.add("show")
	}, 10)
	
	setTimeout(() => {
		toast.classList.remove("show")
		setTimeout(() => {
			toast.style.display = "none"
		}, 300)
	}, 3000)
}
