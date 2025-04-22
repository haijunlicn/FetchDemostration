<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>CineMax - Register</title>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.12.2/gsap.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" />
<link href="${pageContext.request.contextPath}/resources/css/cusRegister.css"
	rel="stylesheet">
<style>
.error-message {
	color: #dc3545;
	font-size: 0.875rem;
	margin-top: 0.25rem;
	display: block;
}

.input-group.has-error input {
	border-color: #dc3545;
	box-shadow: 0 0 0 0.25rem rgba(220, 53, 69, 0.25);
}

.input-group.has-error .input-icon {
	color: #dc3545;
}

.alert {
	border-radius: 8px;
	margin-bottom: 20px;
	padding: 12px 16px;
	animation: fadeIn 0.5s ease-in-out;
}

.alert-danger {
	background-color: rgba(220, 53, 69, 0.15);
	border: 1px solid rgba(220, 53, 69, 0.3);
}

@keyframes fadeIn {
	from {
		opacity: 0;
		transform: translateY(-10px);
	}
	to {
		opacity: 1;
		transform: translateY(0);
	}
}
</style>
</head>
<body>
	<div class="cinema-background"></div>
	<div class="particles" id="particles"></div>

	<c:if test="${not empty error}">
		<div class="alert alert-danger">
			<i class="fas fa-exclamation-circle me-2"></i> ${error}
		</div>
	</c:if>

	<div class="main-container">
		<div class="register-container">
			<a href="${pageContext.request.contextPath}/"
				style="text-decoration: none;">
				<div class="logo">
					<h2>
						Cine<span class="cinemax-highlight">Max</span>
					</h2>
				</div>
			</a>
			<p class="tagline">Experience movies like never before</p>

			<form:form modelAttribute="customer" method="post" action="register">
				<div class="input-group ${not empty customer.name ? '' : 'has-error'}">
					<form:input path="name" class="form-control"
						placeholder="Your Name" />
					<i class="fas fa-user input-icon"></i>
				</div>
				<form:errors path="name" cssClass="error-message" />
				<div class="input-group ${not empty customer.email ? '' : 'has-error'}">
					<form:input path="email" class="form-control"
						placeholder="Your Email Address" />
					<i class="fas fa-envelope input-icon"></i>
				</div>
				<form:errors path="email" cssClass="error-message" />
				<div class="input-group ${not empty customer.password ? '' : 'has-error'}">
					<form:password path="password" class="form-control"
						placeholder="Choose a Password" />
					<i class="fas fa-lock input-icon"></i>
				</div>
				<form:errors path="password" cssClass="error-message" />
				<button type="submit" class="btn btn-register">Sign Up</button>
			</form:form>

			<div class="login-link">
				<a href="cusLogin">Already a CineMax member? Sign in</a>
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
			gsap.from(".register-container", {
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

			gsap.from(".login-link", {
				duration : 0.8,
				opacity : 0,
				y : 20,
				delay : 1.5,
				ease : "power2.out",
			});
			
			// Highlight fields with errors
			document.querySelectorAll('.error-message').forEach(error => {
				if (error.textContent.trim() !== '') {
					const inputGroup = error.previousElementSibling;
					inputGroup.classList.add('has-error');
				}
			});
			
			// Animate error messages
			gsap.from(".error-message", {
				duration: 0.5,
				opacity: 0,
				y: -10,
				stagger: 0.1,
				ease: "power2.out",
			});
			
			gsap.from(".alert", {
				duration: 0.8,
				opacity: 0,
				y: -20,
				ease: "elastic.out(1, 0.5)",
			});
		});
	</script>
</body>
</html>
