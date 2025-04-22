const paymentSelect = document.querySelector('.payment-method-select');
		const paymentDetails = document.querySelectorAll('.payment-details');

		function showSelectedPaymentDetails() {
			const selectedId = paymentSelect.value;
			paymentDetails.forEach(detail => {
				detail.classList.remove('active');
			});
			document.getElementById('payment-' + selectedId).classList.add('active');
		}

		paymentSelect.addEventListener('change', function() {
			this.style.borderColor = '#dc3545';
			showSelectedPaymentDetails();
		});

		function previewReceipt(input) {
			const preview = document.getElementById('receipt-preview');
			if (input.files && input.files[0]) {
				const reader = new FileReader();
				reader.onload = function(e) {
					preview.src = e.target.result;
					preview.classList.add('active');
				}
				reader.readAsDataURL(input.files[0]);
			}
		}

		// Show initial payment details
		showSelectedPaymentDetails();