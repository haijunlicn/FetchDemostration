package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.spring.model.SeatBean;

@Repository
public class SeatRepository {

	public List<SeatBean> getSeatsByCinemaId(int cinemaId) {
		List<SeatBean> seatList = new ArrayList<SeatBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT s.*, st.seat_type, st.size " + "FROM seat s JOIN seat_type st ON s.seat_type_id = st.id "
				+ "WHERE s.cinema_id = ? ORDER BY s.col;";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, cinemaId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				SeatBean obj = new SeatBean();
				obj.setId(rs.getInt("id"));
				obj.setCinemaId(rs.getInt("cinema_id"));
				obj.setSeatNum(rs.getString("seat_num"));
				obj.setRow(rs.getInt("row"));
				obj.setCol(rs.getInt("col"));
				obj.setSeatTypeId(rs.getInt("seat_type_id"));
				obj.setSeatTypeName(rs.getString("seat_type"));
				obj.setSeatSize(rs.getDouble("size"));
				obj.setPrice(rs.getDouble("price"));
				obj.setCurrencyType(rs.getString("currency_type"));
				obj.setStatus(rs.getInt("status"));
				seatList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("getSeatsByCinemaId error : " + e.getMessage());
		}

		return seatList;
	}

	public SeatBean getSeatById(int seatId) {
		SeatBean obj = new SeatBean();

		Connection con = DBConnection.getConnection();
		String sql = "SELECT * from seat WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, seatId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				obj.setId(rs.getInt("id"));
				obj.setCinemaId(rs.getInt("cinema_id"));
				obj.setSeatNum(rs.getString("seat_num"));
				obj.setRow(rs.getInt("row"));
				obj.setCol(rs.getInt("col"));
				obj.setSeatTypeId(rs.getInt("seat_type_id"));
				obj.setPrice(rs.getDouble("price"));
				obj.setCurrencyType(rs.getString("currency_type"));
				obj.setStatus(rs.getInt("status"));
			}

		} catch (SQLException e) {
			System.out.println("get cinema by id error : " + e.getMessage());
		}

		return obj;
	}

	public List<SeatBean> getSeatsByIds(List<Integer> seatIds) {
		List<SeatBean> seatList = new ArrayList<>();

		if (seatIds == null || seatIds.isEmpty()) {
			return seatList; // Return empty list if no seat IDs provided
		}

		String placeholders = seatIds.stream().map(id -> "?").collect(Collectors.joining(", "));
		String sql = "SELECT * FROM seat WHERE id IN (" + placeholders + ")";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			// Set each ID dynamically
			for (int i = 0; i < seatIds.size(); i++) {
				ps.setInt(i + 1, seatIds.get(i));
			}

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					SeatBean seat = new SeatBean();
					seat.setId(rs.getInt("id"));
					seat.setCinemaId(rs.getInt("cinema_id"));
					seat.setSeatNum(rs.getString("seat_num"));
					seat.setRow(rs.getInt("row"));
					seat.setCol(rs.getInt("col"));
					seat.setSeatTypeId(rs.getInt("seat_type_id"));
					seat.setPrice(rs.getDouble("price"));
					seat.setCurrencyType(rs.getString("currency_type"));
					seat.setStatus(rs.getInt("status"));
					seatList.add(seat);
				}
			}

		} catch (SQLException e) {
			System.out.println("Error retrieving seats: " + e.getMessage());
		}

		return seatList;
	}

	public int addNewSeat(SeatBean seatObj) {
		int generatedId = -1; // Default value if insertion fails

		// Get database connection
		Connection con = DBConnection.getConnection();

		// SQL query to insert a new seat
		String sql = "INSERT INTO `seat`(`cinema_id`, `seat_num`, `row`, `col`, `seat_type_id`, `price`, `currency_type`, `status`) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			// Prepare statement with RETURN_GENERATED_KEYS to get the auto-generated ID
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			ps.setInt(1, seatObj.getCinemaId());
			ps.setString(2, seatObj.getSeatNum());
			ps.setInt(3, seatObj.getRow());
			ps.setInt(4, seatObj.getCol());
			ps.setInt(5, seatObj.getSeatTypeId());
			ps.setDouble(6, seatObj.getPrice());
			ps.setString(7, seatObj.getCurrencyType());
			ps.setInt(8, seatObj.getStatus());

			// Execute the query
			int rowsAffected = ps.executeUpdate();

			// If insertion was successful, retrieve the generated key
			if (rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					generatedId = rs.getInt(1); // Get the generated ID
				}
				rs.close();
			}

		} catch (SQLException e) {
			System.out.println("Add seat error: " + e.getMessage());
		}

		return generatedId; // Return the new seat ID
	}

	public int updateSeat(SeatBean seatObj) {
		int i = -1;

		Connection con = DBConnection.getConnection();

		String sql = "UPDATE `seat` SET `seat_num` = ?, `row` = ?, `col` = ?, "
				+ "`seat_type_id` = ?, `price` = ?, `currency_type` = ?, `status` = ? WHERE `id` = ?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, seatObj.getSeatNum());
			ps.setInt(2, seatObj.getRow());
			ps.setInt(3, seatObj.getCol());
			ps.setInt(4, seatObj.getSeatTypeId());
			ps.setDouble(5, seatObj.getPrice());
			ps.setString(6, seatObj.getCurrencyType());
			ps.setInt(7, seatObj.getStatus());
			ps.setInt(8, seatObj.getId());
			i = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("update seat error : " + e.getMessage());
		}

		return i;
	}

	public int checkSeatDuplicate(SeatBean seatObj) {
		int duplicateSeatId = -1;
		Connection con = DBConnection.getConnection();

		// If inserting (id is null or 0), exclude "id != ?" condition
		String sql = (seatObj.getId() != null && seatObj.getId() > 0)
				? "SELECT id FROM `seat` WHERE cinema_id = ? AND row = ? AND col = ? AND id != ?"
				: "SELECT id FROM `seat` WHERE cinema_id = ? AND row = ? AND col = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, seatObj.getCinemaId());
			ps.setInt(2, seatObj.getRow());
			ps.setInt(3, seatObj.getCol());

			// If updating, set the seat ID to exclude itself
			if (seatObj.getId() != null && seatObj.getId() > 0) {
				ps.setInt(4, seatObj.getId());
			}

			ResultSet rs = ps.executeQuery();

			// If a duplicate is found, return its seat ID
			if (rs.next()) {
				duplicateSeatId = rs.getInt("id");
			}

		} catch (SQLException e) {
			System.out.println("Check seat duplicate error: " + e.getMessage());
		}

		return duplicateSeatId; // Returns the duplicate seat ID or null if none found
	}

	public int hardDeleteSeat(int seatId) {
		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "DELETE FROM `seat` WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, seatId);
			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("delete seat error : " + e.getMessage());
		}

		return i;
	}

}
