package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.TownshipBean;

@Repository
public class TownshipRepository {
	public List<TownshipBean> getAllTownship() {
		List<TownshipBean> townshipList = new ArrayList<TownshipBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT t.*, s.name AS state_name FROM township t JOIN state s ON t.state_id = s.id;";

		try {
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				TownshipBean obj = new TownshipBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setStateId(rs.getInt("state_id"));
				obj.setStateName(rs.getString("state_name"));
				obj.setStatus(rs.getInt("status"));
				townshipList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get all township error : " + e.getMessage());
		}

		return townshipList;
	}

	public List<TownshipBean> getAllActiveTownship() {
		List<TownshipBean> townshipList = new ArrayList<TownshipBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT t.*, s.name AS state_name FROM township t "
					+ "JOIN state s ON t.state_id = s.id "
					+ "WHERE t.status = ?;";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, 1);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				TownshipBean obj = new TownshipBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setStateId(rs.getInt("state_id"));
				obj.setStateName(rs.getString("state_name"));
				obj.setStatus(rs.getInt("status"));
				townshipList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get all township error : " + e.getMessage());
		}

		return townshipList;
	}

	public TownshipBean findById(int id) {
		TownshipBean township = new TownshipBean();
		String query = "SELECT * FROM township WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {

			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				township.setId(rs.getInt("id"));
				township.setName(rs.getString("name"));
				township.setStateId(rs.getInt("state_id"));
				township.setStatus(rs.getInt("status"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return township;
	}

	public void update(TownshipBean obj) {
		String query = "UPDATE township SET name = ?, state_id = ? , status = ? WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {

			con.setAutoCommit(false);
			stmt.setString(1, obj.getName());
			stmt.setInt(2, obj.getStateId());
			stmt.setInt(3, obj.getStatus());
			stmt.setInt(4, obj.getId());
			int rowsAffected = stmt.executeUpdate();
			con.commit();

			if (rowsAffected > 0) {
				System.out.println("update successful");
			} else {
				System.out.println("No record found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Soft delete failed");
		}
	}

	public TownshipBean save(TownshipBean township) {
		String query = "INSERT INTO township (name, state_id, status) VALUES (?, ?, ?)";
		try (Connection con = DBConnection.getConnection();
				PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, township.getName());
			if (township.getStateId() == null) {
				throw new IllegalArgumentException("State must not be null!");
			}
			stmt.setInt(2, township.getStateId());
			stmt.setInt(3, township.getStatus());

			int affectedRows = stmt.executeUpdate();
			if (affectedRows > 0) {
				ResultSet generatedKeys = stmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					township.setId(generatedKeys.getInt(1));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return township;
	}

	public void softDelete(int id) {
		String query = "UPDATE township SET status = 0 WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {

			con.setAutoCommit(false);
			stmt.setInt(1, id);
			int rowsAffected = stmt.executeUpdate();
			con.commit();

			if (rowsAffected > 0) {
				System.out.println("Soft delete successful for ID: " + id);
			} else {
				System.out.println("No record found with ID: " + id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Soft delete failed for ID: " + id);
		}
	}

	public void hardDelete(int id) {
		String query = "DELETE FROM township WHERE id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement stmt = con.prepareStatement(query)) {
			con.setAutoCommit(false);
			stmt.setInt(1, id);
			int rowsAffected = stmt.executeUpdate();
			con.commit();

			if (rowsAffected > 0) {
				System.out.println("Hard delete successful for ID: " + id);
			} else {
				System.out.println("No record found with ID: " + id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Hard delete failed for ID: " + id);
		}
	}

}
