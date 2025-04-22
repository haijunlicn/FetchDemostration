/*$(document).ready(function() {
	function initializeDataTable(selector, options) {
		const tableElement = $(selector);
		if (tableElement.length) {
			const dataTable = new simpleDatatables.DataTable(tableElement[0], options);

			// Customize search box placeholder
			tableElement.parent().find(".datatable-input").attr("placeholder", "Try searching...");
		}
	}

	// Initialize both tables with different settings
	initializeDataTable('#datatablesSimple', {
		searchable: true,
		fixedHeight: true,
		paging: false,
		perPage: 0,
		perPageSelect: false,
		labels: { info: "" }
	});

	initializeDataTable('#smallDatatable', {
		searchable: true,
		fixedHeight: true,
		perPage: 5,
		perPageSelect: [5, 10, 15, 20]
	});
});*/

/*document.addEventListener("DOMContentLoaded", function() {
	function initializeDataTable(selector, options) {
		const tableElement = document.querySelector(selector);
		if (tableElement) {
			const dataTable = new simpleDatatables.DataTable(tableElement, options);

			// Customize search box placeholder
			const searchInput = tableElement.closest(".datatable-wrapper")?.querySelector(".datatable-input");
			if (searchInput) {
				searchInput.placeholder = "Try searching...";
			}
		}
	}

	// Initialize both tables with different settings
	initializeDataTable('#datatablesSimple', {
		searchable: true,
		fixedHeight: true,
		paging: false,
		perPage: 0,
		perPageSelect: false,
		labels: { info: "" }
	});

	initializeDataTable('#smallDatatable', {
		searchable: true,
		fixedHeight: true,
		perPage: 5,
		perPageSelect: [5, 10, 15, 20]
	});
});
*/

document.addEventListener("DOMContentLoaded", function() {
	function initializeDataTable(selector, options) {
		var tableElement = document.querySelector(selector);
		if (tableElement) {
			var dataTable = new simpleDatatables.DataTable(tableElement, options);

			// Find the search input inside the datatable wrapper
			var wrapper = tableElement.closest(".datatable-wrapper");
			if (wrapper) {
				var searchInput = wrapper.querySelector(".datatable-input");
				if (searchInput) {
					searchInput.setAttribute("placeholder", "Try searching...");
				}
			}
		}
	}

	// Initialize both tables with different settings
	initializeDataTable("#datatablesSimple", {
		searchable: true,
		fixedHeight: true,
		paging: false,
		perPage: 0,
		perPageSelect: false,
		labels: { info: "" }
	});

	initializeDataTable("#smallDatatable", {
		searchable: true,
		fixedHeight: true,
		perPage: 5,
		perPageSelect: [5, 10, 15, 20]
	});
});


