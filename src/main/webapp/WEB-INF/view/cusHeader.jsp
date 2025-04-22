<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cinema Booking</title>

<!-- Bootstrap 5 CSS (Single Source) -->
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">

<!-- Font Awesome (Single Source) -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

<!-- Google Fonts -->
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap"
	rel="stylesheet">

<!-- Custom Stylesheets -->
<link
	href="${pageContext.request.contextPath}/resources/cusTemplate/css/global.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/cusTemplate/css/index.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/resources/css/homepage.css"
	rel="stylesheet">

<!-- jQuery (Single Source) -->
<script
	src="${pageContext.request.contextPath}/resources/js/jquery-3.7.1.min.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/js/filterMovieDatepicker.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<!-- Bootstrap Bundle JS (Single Source) -->
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Bootstrap Datepicker -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"></script>

<!-- Custom Scripts -->
<script
	src="${pageContext.request.contextPath}/resources/js/cusSidebar.js"
	defer></script>

<style>
body {
	padding-top: 70px;
	background-color: #0a0a0a;
	color: #ffffff;
}

.cinemax-highlight {
	color: #dc3545;
	font-weight: 700;
}

.logo {
	margin-bottom: 5px;
	position: relative;
}

.logo::after {
	content: "";
	position: absolute;
	bottom: -7px;
	left: 50%;
	transform: translateX(-50%);
	width: 80px;
	height: 3px;
	background: linear-gradient(90deg, transparent, rgba(220, 53, 69, 0.8),
		transparent);
}
</style>
</head>
<body>
	<nav class="navbar navbar-expand-lg fixed-top">
		<div class="container-fluid">
			<button type="button" id="sidebarCollapse" class="navbar-btn">
				<i class="fa fa-bars fa-2x"></i>
			</button>
			<a class="navbar-brand" href="${pageContext.request.contextPath}/">
				<div class="logo">
					<h2>
						Cine<span class="cinemax-highlight">Max</span>
					</h2>
				</div>
			</a>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav ms-auto">
					<li class="nav-item"><a class="nav-link"
						href="${pageContext.request.contextPath}/">Home</a></li>
					<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/movies">Movies</a></li>

					<c:if
						test="${not empty sessionScope.accountInfo and sessionScope.role eq 'CUSTOMER'}">
						<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/account/myBookings">My
								Bookings</a></li>
					</c:if>

					<li class="nav-item"><a class="nav-link" href="about.html">About</a></li>
				</ul>
				<div class="auth-buttons">
					<c:choose>
						<c:when
							test="${not empty sessionScope.accountInfo and sessionScope.role eq 'CUSTOMER'}">
							<div class="dropdown">
								<button class="btn btn-outline-light dropdown-toggle"
									type="button" id="userDropdown" data-bs-toggle="dropdown"
									aria-expanded="false">
									<i class="fas fa-user-circle me-1"></i>${sessionScope.accountInfo.name}
								</button>
								<ul class="dropdown-menu dropdown-menu-end"
									aria-labelledby="userDropdown">
									<li><a class="dropdown-item"
										href="${pageContext.request.contextPath}/profile"><i
											class="fas fa-user me-2"></i> My Profile</a></li>
									<li><hr class="dropdown-divider"></li>
									<li><a class="dropdown-item text-danger"
										href="${pageContext.request.contextPath}/auth/cusLogout"><i
											class="fas fa-sign-out-alt me-2"></i> Sign Out</a></li>
								</ul>
							</div>
						</c:when>
						<c:otherwise>
							<a href="${pageContext.request.contextPath}/auth/cusLogin"
								class="auth-btn login-btn">Login</a>
							<a href="${pageContext.request.contextPath}/auth/register"
								class="auth-btn register-btn">Register</a>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</nav>
</body>
</html>
