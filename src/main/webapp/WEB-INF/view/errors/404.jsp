<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<title>404 Error</title>
<link href="${pageContext.request.contextPath}/resources/adminTemplate/css/styles.css"
	rel="stylesheet" />
<script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
	crossorigin="anonymous"></script>
</head>
<body>
	<div id="layoutError">
		<div id="layoutError_content">
			<main>
				<div class="container">
					<div class="row justify-content-center">
						<div class="col-lg-6">
							<div class="text-center mt-4">
								<img class="mb-4 img-error"
									src="${pageContext.request.contextPath}/resources/adminTemplate/assets/img/error-404-monochrome.svg" />
								<p class="lead">This requested URL was not found on this
									server.</p>
								<a href="${pageContext.request.contextPath}/"> <i class="fas fa-arrow-left me-1"></i>
									Return to Homepage
								</a>
							</div>
						</div>
					</div>
				</div>
			</main>
		</div>
		<div id="layoutError_footer">
			<footer class="py-4 bg-light mt-auto">
				<div class="container-fluid px-4">
					<div
						class="d-flex align-items-center justify-content-between small">
						<div class="text-muted">Copyright &copy; Your Website 2023</div>
						<div>
							<a href="#">Privacy Policy</a> &middot; <a href="#">Terms
								&amp; Conditions</a>
						</div>
					</div>
				</div>
			</footer>
		</div>
	</div>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
		crossorigin="anonymous"></script>
	<script src="js/scripts.js"></script>
</body>
</html>


<%-- <!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>404 - Page Not Found</title>

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
}

.error-code {
	font-size: 4rem;
	font-weight: bold;
	color: #dc3545;
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
			<h1 class="error-code">404</h1>
			<p class="error-text">The page you are looking for does not
				exist.</p>
			<a href="<%=request.getContextPath()%>/" class="btn btn-danger">Back
				to Home</a>
		</div>
	</div>

	<!-- Bootstrap 5 JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> --%>
