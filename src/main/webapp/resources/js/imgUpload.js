function displaySelectedImage(event, elementId) {
	const selectedImage = document.getElementById(elementId);
	const fileInput = event.target;

	if (fileInput.files && fileInput.files[0]) {
		const reader = new FileReader();

		reader.onload = function(e) {
			selectedImage.src = e.target.result;
		};

		reader.readAsDataURL(fileInput.files[0]);
	}
}

function resetImage() {
	const selectedImage = document.getElementById('selectedAvatar');
	const fileInput = document.getElementById('customFile2');

	// Reset the image to the placeholder
	selectedImage.src = "https://mdbootstrap.com/img/Photos/Others/placeholder-avatar.jpg";

	// Clear the file input
	fileInput.value = ""; // This will clear the selected file
}

function previewImages(event, containerId) {
	const container = document.getElementById(containerId);
	container.innerHTML = ""; // Clear previous previews

	const files = event.target.files;
	if (files.length > 0) {
		for (let i = 0; i < files.length; i++) {
			const file = files[i];
			const reader = new FileReader();

			reader.onload = function(e) {
				const imgElement = document.createElement("img");
				imgElement.src = e.target.result;
				imgElement.classList.add("rounded", "border", "shadow-sm");
				imgElement.style.width = "120px";
				imgElement.style.height = "120px";
				imgElement.style.objectFit = "cover";

				container.appendChild(imgElement);
			};

			reader.readAsDataURL(file);
		}
	}
}

function markImageForDeletion(imageId, button) {
	let deletedImagesInput = document.getElementById("deletedImages");

	// Get existing deleted image IDs
	let deletedImages = deletedImagesInput.value ? deletedImagesInput.value.split(",").map(Number) : [];

	// Convert imageId to a number before checking
	imageId = Number(imageId);

	// Add to list if not already present
	if (!deletedImages.includes(imageId)) {
		deletedImages.push(imageId);
	}

	// Update hidden input field with IDs
	deletedImagesInput.value = deletedImages.join(",");

	// Remove image from UI
	button.parentElement.remove();
}

window.addEventListener("load", function() {
	/*	document.getElementById("deletedImages").value = "";*/

	const deletedImagesElement = document.getElementById("deletedImages");
	if (deletedImagesElement) {
		deletedImagesElement.value = "";
	}

});

/*function handleVideoUpload(event) {
	const file = event.target.files[0]; // Get the uploaded file

	// Check if a file is selected
	if (file) {
		const videoSource = document.getElementById("videoSource");
		const videoPreview = document.getElementById("videoPreview");

		// Create a URL for the video file and update the video source
		const videoURL = URL.createObjectURL(file);
		videoSource.src = videoURL;

		// Reload the video preview
		videoPreview.load();

		// Make sure the video preview is visible
		videoPreview.classList.remove("d-none");
	} else {
		// If no file is selected, hide the video preview
		const videoPreview = document.getElementById("videoPreview");
		videoPreview.classList.add("d-none");

		// Optionally, you can clear the video preview source
		const videoSource = document.getElementById("videoSource");
		videoSource.src = "";
	}
}
*/

function handleVideoUpload(event) {
	const file = event.target.files[0];
	if (file) {
		const videoSource = document.getElementById("videoSource");
		const videoPreview = document.getElementById("videoPreview");
		videoSource.src = URL.createObjectURL(file);
		videoPreview.load();
		videoPreview.classList.remove("d-none");
	}
}




