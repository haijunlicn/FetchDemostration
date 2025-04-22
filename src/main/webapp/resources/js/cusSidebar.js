/**
 * 
 */

$(document).ready(function() {
	$('#sidebarCollapse').on('click', function() {
		$('#sidebar').toggleClass('active');
		$('.overlay').toggleClass('active');
	});

	$('.overlay').on('click', function() {
		$('#sidebar').removeClass('active');
		$('.overlay').removeClass('active');
	});
});