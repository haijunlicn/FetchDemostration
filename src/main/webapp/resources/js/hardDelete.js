function confirmDeletion(url) {
	Swal.fire({
		title: 'Are you sure?',
		text: 'This action cannot be undone!',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#d33',
		cancelButtonColor: '#3085d6',
		confirmButtonText: 'Yes, delete it!',
		cancelButtonText: 'Cancel'
	}).then((result) => {
		if (result.isConfirmed) {
			window.location.href = url;
		}
	});
}

