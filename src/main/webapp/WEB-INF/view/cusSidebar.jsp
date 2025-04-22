<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>CineMax Menu</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
<style>
/* Overlay */
.overlay {
	position: fixed;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.7);
	z-index: 998;
	display: none;
	opacity: 0;
	transition: all 0.5s ease-in-out;
}

.overlay.active {
	display: block;
	opacity: 1;
}

/* Sidebar */
#sidebar {
	position: fixed;
	top: 0;
	left: -250px;
	height: 100vh;
	width: 250px;
	min-width: 250px;
	max-width: 250px;
	background: rgba(0, 0, 0, 0.95);
	color: #fff;
	transition: all 0.3s;
	z-index: 999;
	padding-top: 60px;
}

#sidebar.active {
	left: 0;
}

#sidebar .sidebar-header {
	padding: 20px;
	background: rgba(229, 9, 20, 0.1);
}

#sidebar ul.components {
	padding: 20px 0;
	border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

#sidebar ul li a {
	padding: 10px 20px;
	font-size: 1.1em;
	display: block;
	color: #fff;
	text-decoration: none;
	transition: all 0.3s;
}

#sidebar ul li a:hover {
	color: var(--primary);
	background: rgba(255, 255, 255, 0.1);
}

#sidebar ul li.active>a {
	color: var(--primary);
	background: rgba(255, 255, 255, 0.1);
}

#sidebar ul li a i {
	margin-right: 10px;
}

/* Sidebar Toggle Button */
#sidebarCollapse {
	background: transparent;
	border: none;
	color: var(--light);
	margin-right: 15px;
}

#sidebarCollapse:hover {
	color: var(--primary);
}
</style>
</head>
<body>
	<div class="overlay"></div>
	
	<!-- Sidebar -->
	<nav id="sidebar">
		<div class="sidebar-header">
			<h3>CineMax Menu</h3>
		</div>

		<ul class="list-unstyled components">
			<li class="active">
				<a href="${pageContext.request.contextPath}/">
					<i class="fas fa-home"></i> Home
				</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/movies">
					<i class="fas fa-film"></i> Movies
				</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/account/myBookings">
					<i class="fas fa-ticket-alt"></i> My Bookings
				</a>
			</li>
			<li>
				<a href="/">
					<i class="fas fa-info-circle"></i> About
				</a>
			</li>
			<li>
				<a href="#">
					<i class="fas fa-envelope"></i> Contact
				</a>
			</li>
		</ul>
	</nav>

</body>
</html>