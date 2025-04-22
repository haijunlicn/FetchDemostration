function confirmBooking(event) {
	event.preventDefault();

	const selectedSeats = document.querySelectorAll('.seat.selected');
	if (selectedSeats.length === 0) {
		Swal.fire({
			title: 'No Seats Selected',
			text: 'Please select at least one seat to proceed with booking.',
			icon: 'warning',
			confirmButtonText: 'OK'
		});
		return false;
	}

	Swal.fire({
		title: 'Confirm Booking',
		text: 'Are you sure you want to proceed with this booking?',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonText: 'Yes, Proceed to payment.',
		cancelButtonText: 'Cancel',
		reverseButtons: true
	}).then((result) => {
		if (result.isConfirmed) {
			document.getElementById('seatBookingForm').submit();
		}
	});

	return false;
}