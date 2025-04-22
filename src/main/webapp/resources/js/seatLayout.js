const seats = document.querySelectorAll(".seat-btn");
const addSeatBtns = document.querySelectorAll(".addSeatBtn");
const seatDetailsCard = document.getElementById("seatDetailsCard");

var isUpdateForm = false;
console.log(isUpdateForm);

// Get seatId from URL parameters
const urlParams = new URLSearchParams(window.location.search);
const seatIdFromUrl = urlParams.get('selectedSeatId');

// Check if there's a `selectedSeatId` passed from the model
const selectedSeatId = document.getElementById("selectedSeatId") ? document.getElementById("selectedSeatId").value : null;

// Function to clear the form
function clearForm() {
	document.getElementById("seatId").value = "";
	document.getElementById("seatNum").value = "";
	document.getElementById("row").value = "";
	document.getElementById("col").value = "";
	document.getElementById("seatTypeId").value = "";
	document.getElementById("price").value = "";
	document.getElementById("currencyType").value = "";
	document.getElementById("status").value = "1"; // Default to Available
	if (isUpdateForm) {
		document.getElementById("deleteButton").style.display = "block";
		document.getElementById("cancelButton").style.display = "none";
	} else {
		document.getElementById("deleteButton").style.display = "none";
		document.getElementById("cancelButton").style.display = "block";
	}
}

addSeatBtns.forEach(addSeatBtn => {
	addSeatBtn.addEventListener("click", function() {
		isUpdateForm = false;
		console.log(isUpdateForm);
		clearForm();

		const cinemaId = this.getAttribute("data-cinemaid");
		const rowNum = this.getAttribute('data-row');
		const colNum = calculateNextColNum(rowNum);

		// Show the seat details form
		seatDetailsCard.style.display = "block";

		// Pre-fill the form with the row and calculated column number
		document.getElementById("row").value = rowNum;
		document.getElementById("col").value = colNum;
		document.getElementById("cinemaId").value = cinemaId;
	});
});

function calculateNextColNum(rowNum) {
	const seatsInRow = document.querySelectorAll(`.seat-btn[data-row="${rowNum}"]`);
	let maxCol = 0;

	seatsInRow.forEach(seat => {
		const colNum = parseInt(seat.getAttribute('data-col'));
		if (colNum > maxCol) {
			maxCol = colNum;
		}
	});

	return maxCol + 1; // Next column number
}



seats.forEach(seat => {
	seat.addEventListener("click", function() {
		isUpdateForm = true;
		console.log(isUpdateForm);
		// Clear previous form data
		clearForm();

		// Get seat data
		const seatId = this.getAttribute("data-seatid");
		const cinemaId = this.getAttribute("data-cinemaid");
		const seatNum = this.getAttribute("data-seatnum");
		const rowNum = this.getAttribute("data-row");
		const colNum = this.getAttribute("data-col");
		const seatTypeId = this.getAttribute("data-seattypeid");
		const price = this.getAttribute("data-price");
		const currencyType = this.getAttribute("data-currencytype");
		const status = this.getAttribute("data-status");

		// Show the seat details form
		seatDetailsCard.style.display = "block";

		// Pre-fill the form with the selected seat data
		document.getElementById("seatId").value = seatId;
		document.getElementById("cinemaId").value = cinemaId;
		document.getElementById("seatNum").value = seatNum;
		document.getElementById("row").value = rowNum;
		document.getElementById("col").value = colNum;
		document.getElementById("seatTypeId").value = seatTypeId;
		document.getElementById("price").value = price;
		document.getElementById("currencyType").value = currencyType;
		document.getElementById("status").value = status;

		// Highlight the selected seat
		document.querySelectorAll(".seat-btn").forEach(seat => seat.classList.remove("selected"));
		this.classList.add("selected");
	});
});

// If seatId exists in the URL or `selectedSeatId` from the backend, highlight the corresponding seat
if (seatIdFromUrl || selectedSeatId) {
	const seatToHighlight = document.querySelector(`.seat-btn[data-seatid="${seatIdFromUrl || selectedSeatId}"]`);
	if (seatToHighlight) {
		// Highlight the seat
		seatToHighlight.classList.add("selected");

		// Show the seat details form
		seatDetailsCard.style.display = "block";
		isUpdateForm = true;
		console.log(isUpdateForm);

		// Trigger a click event on the seat to simulate the user interaction
		seatToHighlight.click();
	}
}

function confirmDelete(deleteUrl) {
	const seatId = document.getElementById("seatId").value; // Get the seat ID from the hidden input
	const cinemaId = document.getElementById("cinemaId").value; // Get the cinema ID from the hidden input

	// Construct the delete URL with the seat ID
	const fullDeleteUrl = `${deleteUrl}${cinemaId}/${seatId}`; // Corrected string interpolation

	Swal.fire({
		title: "Are you sure?",
		text: "You won't be able to revert this!",
		icon: "warning",
		showCancelButton: true,
		confirmButtonColor: "#d33",
		cancelButtonColor: "#3085d6",
		confirmButtonText: "Yes, delete it!"
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = fullDeleteUrl; // Redirect to the constructed URL
		}
	});
}
