package com.spring.repository;

import com.spring.model.Customer;
import com.spring.repository.DBConnection; 
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository

public class CustomerRepository {

	public void save(Customer customer) {
	    Connection con = DBConnection.getConnection();
	    PreparedStatement ps = null;

	    try {
	        String sql = "INSERT INTO account (name, email, password, role_Id) " +
	                     "VALUES (?, ?, ?, ?)";
	        ps = con.prepareStatement(sql);

	        ps.setString(1, customer.getName());
	        ps.setString(2, customer.getEmail());
	        ps.setString(3, customer.getPassword()); 
	        ps.setInt(4, 3);

	        int rowsAffected = ps.executeUpdate();
	        System.out.println("Rows inserted: " + rowsAffected);  // Log rows affected
	    } catch (SQLException e) {
	        e.printStackTrace(); 
	    } finally {
	        closeResources(con, ps, null); 
	    }
	}


    public Customer findByEmail(String email) {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Customer customer = null;

        try {
            String sql = "SELECT * FROM account WHERE email = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                customer = new Customer(); 
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPassword(rs.getString("password")); 
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        } finally {
            closeResources(con, ps, rs);
        }

        return customer;
    }

    

    private void closeResources(Connection con, PreparedStatement ps, ResultSet rs) {
        try { if (rs != null) rs.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (ps != null) ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        try { if (con != null) con.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public boolean existsByEmail(String email) {
        Connection con = DBConnection.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            String sql = "SELECT COUNT(*) FROM account WHERE email = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                exists = rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(con, ps, rs);
        }

        return exists;
    }

}