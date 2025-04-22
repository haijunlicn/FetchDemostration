$(document).ready(function() {
	// Access the input value from the HTML directly
	var selectedDate = $('#dateInput').val();  // Get the value from the input field
	
	// Initialize the datepicker
	$('.datepicker').datepicker({
		format: 'dd/mm/yyyy',  // Set the format to 'DD/MM/YYYY'
		autoclose: true,        // Close the calendar when a date is selected
		todayHighlight: true,   // Highlight the current date
		startDate: 'today'      // Optional: Disable past dates
	});

	$('.input-group-text').click(
		function() {
			$(this).closest('.input-group').find(
				'.datepicker').datepicker('show');
		});

	// If the selectedDate is not empty and needs formatting, set it
	if (selectedDate) {
		var formattedDate = selectedDate.split('-').reverse().join('/'); // For example, converting 'yyyy-mm-dd' to 'dd/mm/yyyy'
		$('.datepicker').datepicker('setDate', formattedDate);
	}
});