<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Unauthorized Access</title>

<!-- Bootstrap 5 CDN -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
	rel="stylesheet">

<style>
body {
	background-color: #f8f9fa;
	height: 100vh;
	display: flex;
	justify-content: center;
	align-items: center;
}

.error-container {
	text-align: center;
	max-width: 500px;
	padding: 20px;
	background: white;
	border-radius: 10px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.error-code {
	font-size: 4rem;
	font-weight: bold;
	color: #ffc107;
}

.error-text {
	font-size: 1.2rem;
	color: #6c757d;
}
</style>
</head>
<body>

	<div class="container">
		<div class="error-container">
			<h1 class="error-code">401</h1>
			<p class="error-text">Oops! You are not authorized to access this
				page.</p>
			<a
				href="<%=request.getHeader("Referer") != null ? request.getHeader("Referer") : request.getContextPath() + "/"%>"
				class="btn btn-warning">Back to Previous Page</a>
		</div>
	</div>

	<!-- Bootstrap 5 JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
