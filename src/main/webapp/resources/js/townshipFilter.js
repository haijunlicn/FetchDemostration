document.addEventListener("DOMContentLoaded", function() {
	// Retrieve JSON data from the div element
	var townshipJson = document.getElementById("townshipData").getAttribute("data-townships");
	var selectedTownshipId = document.getElementById("selectedTownshipId").getAttribute("data-townships");
	var allTownships = JSON.parse(townshipJson); // Convert string to JavaScript array

	var townshipDropdown = document.getElementById("township");
	var stateDropdown = document.getElementById("state");

	var selectedStateId = stateDropdown.value; // Get the currently selected state (if any)

	console.log('selected township id : ' + selectedTownshipId);
	
	populateTownships(selectedStateId); // Populate the townships on load

	// Event listener for the state dropdown change
	stateDropdown.addEventListener("change", function() {
		var selectedStateId = this.value;
		populateTownships(selectedStateId);
	});

	// Function to populate the township dropdown based on the selected state
	function populateTownships(selectedStateId) {
		townshipDropdown.innerHTML = '<option value="">Select a Township</option>'; // Reset options

		if (selectedStateId) {
			var filteredTownships = allTownships.filter(t => t.stateId == selectedStateId);
			filteredTownships.forEach(function(township) {
				var option = document.createElement("option");
				option.value = township.id;
				option.textContent = township.name;
				if (township.id == selectedTownshipId) {
					option.selected = true;
				}
				townshipDropdown.appendChild(option);
			});

			townshipDropdown.disabled = filteredTownships.length === 0;
		} else {
			townshipDropdown.disabled = true;
		}
	}

	if (selectedTownshipId) {
		townshipDropdown.value = selectedTownshipId;
	}

});
