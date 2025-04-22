package com.spring.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	public static Connection getConnection() {
		Connection con = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema_ticket2","root","");
			
		} catch (ClassNotFoundException e) {
			System.out.println("Driver error : " + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Connection error : " + e.getMessage());
		}
		
		return con;
	}
	
}
