package com.spring.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Initialization code, if needed
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		// HttpSession session = httpRequest.getSession(false);

		HttpSession session = httpRequest.getSession(true);

		String adminLoginURI = httpRequest.getContextPath() + "/auth/adminLogin";
		String customerLoginURI = httpRequest.getContextPath() + "/auth/cusLogin";
		String errorPage = httpRequest.getContextPath() + "/auth/error";

		String requestURI = httpRequest.getRequestURI();

		boolean loggedIn = (session != null && session.getAttribute("accountInfo") != null);
		String userRole = (loggedIn) ? (String) session.getAttribute("role") : null;
		
		System.out.println("URI : " + requestURI);
		System.out.println("userRole : " + userRole);

		boolean adminLoginRequest = requestURI.equals(adminLoginURI);
		boolean customerLoginRequest = requestURI.equals(customerLoginURI);

		if (loggedIn) {
			if (isAuthorized(userRole, requestURI, httpRequest)) {
				chain.doFilter(request, response);
			} else {
				System.out.println("error page");
				httpResponse.sendRedirect(errorPage);
			}
		} else if (adminLoginRequest || customerLoginRequest) {
			chain.doFilter(request, response);
		} else {
			if (requestURI.startsWith(httpRequest.getContextPath() + "/admin")) {
				httpResponse.sendRedirect(adminLoginURI);
			} else if (requestURI.startsWith(httpRequest.getContextPath() + "/booking")
					|| requestURI.startsWith(httpRequest.getContextPath() + "/account")) {
				httpResponse.sendRedirect(customerLoginURI);
			} else {
				httpResponse.sendRedirect(errorPage);
			}
		}
	}

	private boolean isAuthorized(String role, String uri, HttpServletRequest request) {
		System.out.println("role : " + role + ", URI : " + uri);

		if (role == null) {
			return false;
		}

		String contextPath = request.getContextPath();
		if (role.equals("SUPER_ADMIN") && uri.startsWith(contextPath + "/admin/")) {
			return true; // SUPER_ADMIN can access anything under /admin/
		}

		// CINEMA_ADMIN can access only specific pages under /admin/cinema/
		if (role.equals("CINEMA_ADMIN")) {
			if (uri.startsWith(contextPath + "/admin/cinema/cinemaManagement")
					|| uri.startsWith(contextPath + "/admin/movie/movieManagement")
					|| uri.startsWith(contextPath + "/admin/showtime/showtimeManagement")
					|| uri.startsWith(contextPath + "/admin/seatType/seatTypeManagement")
					|| uri.startsWith(contextPath + "/admin/bookingManagement")
					|| uri.startsWith(contextPath + "/admin/cinemaAdmin/")
					|| uri.startsWith(contextPath + "/admin/seat/")) {
				return true;
			}
		}
		
		if (role.equals("CUSTOMER")) {
			if (uri.startsWith(contextPath + "/booking")
				|| uri.startsWith(contextPath + "/account")) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void destroy() {
		// Cleanup code, if needed
	}

}