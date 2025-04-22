<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>CineMax - Login</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.2/gsap.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
<link href="${pageContext.request.contextPath}/resources/css/cusLogin.css" rel="stylesheet">
</head>
<body>
	<div class="cinema-background"></div>
	<div class="particles" id="particles"></div>

	<c:if test="${not empty error}">
		<div class="alert alert-danger">
			<i class="fas fa-exclamation-circle me-2"></i>${error}
		</div>
	</c:if>

	<div class="main-container">
		<div class="login-container">
			<a href="${pageContext.request.contextPath}/"
				style="text-decoration: none;">
				<div class="logo">
					<h2>
						Cine<span class="cinemax-highlight">Max</span>
					</h2>
				</div>
			</a>
			<p class="tagline">Welcome back to the movies</p>

			<form:form modelAttribute="accObj" method="post" action="cusLogin">
				<form:hidden path="roleId" value="3" />
				<div class="input-group">
					<form:input path="email" class="form-control"
						placeholder="Your Email Address" />
					<i class="fas fa-envelope input-icon"></i>
					<form:errors path="email" class="text-danger" />
				</div>

				<div class="input-group">
					<form:password path="password" class="form-control"
						placeholder="Your Password" />
					<i class="fas fa-lock input-icon"></i>
					<form:errors path="password" class="text-danger" />
				</div>

				<button type="submit" class="btn btn-login">Sign In</button>
			</form:form>

			<div class="register-link">
				<a href="register">New to CineMax? Create an account</a>
			</div>
			
			<div class="admin-link mt-3 text-center">
				<a href="${pageContext.request.contextPath}/admin/login" class="text-secondary">
					<i class="fas fa-user-shield me-1"></i>Admin Login
				</a>
			</div>
		</div>
	</div>

	<script>
		// Create floating particles
		const particlesContainer = document.getElementById("particles");
		for (let i = 0; i < 50; i++) {
			const particle = document.createElement("div");
			particle.classList.add("particle");
			particle.style.left = `${Math.random() * 100}%`;
			particle.style.animationDuration = `${15 + Math.random() * 20}s`;
			particle.style.animationDelay = `${Math.random() * 5}s`;
			particle.style.opacity = Math.random() * 0.5 + 0.3;
			particlesContainer.appendChild(particle);
		}

		// GSAP animations
		document.addEventListener("DOMContentLoaded", function() {
			gsap.from(".login-container", {
				duration : 1.2,
				y : 50,
				opacity : 0,
				rotationX : 15,
				ease : "power3.out",
			});

			gsap.from(".logo, h2", {
				duration : 1,
				y : -30,
				opacity : 0,
				delay : 0.3,
				ease : "back.out(1.7)",
			});

			gsap.from(".tagline", {
				duration : 1,
				opacity : 0,
				delay : 0.5,
				ease : "power2.out",
			});

			gsap.from(".input-group", {
				duration : 0.8,
				opacity : 0,
				y : 20,
				stagger : 0.2,
				delay : 0.7,
				ease : "power2.out",
			});

			gsap.from(".register-link, .admin-link", {
				duration : 0.8,
				opacity : 0,
				y : 20,
				delay : 1.5,
				ease : "power2.out",
			});
		});
	</script>
</body>
</html>
