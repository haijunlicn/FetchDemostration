document.addEventListener("DOMContentLoaded", function() {
	const rows = document.querySelectorAll(".seat-row");

	rows.forEach((row, rowIndex) => {
		// Add spacing every 3rd row, but only if there's a next row
		if ((rowIndex + 1) % 3 === 0 && rowIndex + 1 < rows.length) {
			row.classList.add("mb-4");
		}

		const seats = row.querySelectorAll(".seat");
		seats.forEach((seat, colIndex) => {
			// Add spacing every 3rd column, but only if there's a next column
			if ((colIndex + 1) % 3 === 0 && colIndex + 1 < seats.length) {
				seat.classList.add("me-4");
			}
		});
	});
})

