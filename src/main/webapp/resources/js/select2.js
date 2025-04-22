$(document).ready(function() {
	// Function to initialize the Select2 and sync checkboxes
	function initializeDropdown(dropdownSelector, checkboxSelector) {
		// Initialize Select2 for the given dropdown
		$(dropdownSelector).select2({
			placeholder: "Select items",
			allowClear: true,
			width: '100%'
		});

		// Set initial selected values in Select2 based on checked checkboxes
		let selectedValues = [];
		$(checkboxSelector + ":checked").each(function() {
			selectedValues.push($(this).val());
		});

		$(dropdownSelector).val(selectedValues).trigger("change");

		// Sync checkboxes when the Select2 dropdown changes
		$(dropdownSelector).on("change", function() {
			let selectedValues = $(this).val() || [];
			$(checkboxSelector).each(function() {
				$(this).prop("checked", selectedValues.includes($(this).val()));
			});
		});
	}

	// Call the function for specific dropdowns and checkboxes
	initializeDropdown("#castDropdown", "input[name='selectedCastIdList']");
	initializeDropdown("#featureDropdown", "input[name='selectedFeatureIdList']");
});


/*$(document).ready(function() {
	$("#castDropdown").select2({
		placeholder: "Select cast members",
		allowClear: true,
		width: '100%'
	});

	// Set initial selected values in Select2 based on checked checkboxes
	let selectedValues = [];
	$("input[name='selectedCastIdList']:checked").each(function() {
		selectedValues.push($(this).val());
	});

	$("#castDropdown").val(selectedValues).trigger("change");

	// Sync checkboxes when Select2 changes
	$("#castDropdown").on("change", function() {
		let selectedValues = $(this).val() || [];

		$("input[name='selectedCastIdList']").each(function() {
			$(this).prop("checked", selectedValues.includes($(this).val()));
		});
	});
});*/