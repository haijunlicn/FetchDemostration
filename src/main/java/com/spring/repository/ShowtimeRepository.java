package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.CinemaBean;
import com.spring.model.ShowtimeBean;

@Repository
public class ShowtimeRepository {
	public List<ShowtimeBean> getAllShowtime() {
		List<ShowtimeBean> showtimeList = new ArrayList<ShowtimeBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT * FROM showtime ORDER BY start_hour";

		try {
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ShowtimeBean obj = new ShowtimeBean();
				obj.setId(rs.getInt("id"));
				obj.setStartHour(rs.getTime("start_hour"));
				obj.setEndHour(rs.getTime("end_hour"));
				obj.setStatus(rs.getInt("status"));
				showtimeList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get all showtime error : " + e.getMessage());
		}

		return showtimeList;
	}

	public List<ShowtimeBean> getActiveShowtimes() {
		List<ShowtimeBean> showtimeList = new ArrayList<>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT * FROM showtime WHERE status = 1 ORDER BY start_hour";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ShowtimeBean obj = new ShowtimeBean();
				obj.setId(rs.getInt("id"));
				obj.setStartHour(rs.getTime("start_hour"));
				obj.setEndHour(rs.getTime("end_hour"));
				obj.setStatus(rs.getInt("status"));
				showtimeList.add(obj);
			}
		} catch (SQLException e) {
			System.out.println("get active showtime error: " + e.getMessage());
		}

		return showtimeList;
	}

	public int addNewShowtime(ShowtimeBean showtimeObj) {
		int i = -1;

		Connection con = DBConnection.getConnection();

		String sql = "INSERT INTO `showtime`(`start_hour`, `end_hour`, `status`) VALUES (?,?,?)";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setTime(1, Time.valueOf(showtimeObj.getLocalStartHour()));
			ps.setTime(2, Time.valueOf(showtimeObj.getLocalEndHour()));
			ps.setInt(3, showtimeObj.getStatus());
			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("add showtime error : " + e.getMessage());
		}

		return i;
	}

	public int updateShowtime(ShowtimeBean showtimeObj) {
		int i = -1;

		Connection con = DBConnection.getConnection();

		String sql = "UPDATE `showtime` SET `start_hour` = ?, `end_hour` = ?, `status` = ? WHERE `id` = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setTime(1, Time.valueOf(showtimeObj.getLocalStartHour()));
			ps.setTime(2, Time.valueOf(showtimeObj.getLocalEndHour()));
			ps.setInt(3, showtimeObj.getStatus());
			ps.setInt(4, showtimeObj.getId());
			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("update showtime error : " + e.getMessage());
		}

		return i;
	}

	public ShowtimeBean getShowtimeById(int showtimeId) {
		ShowtimeBean obj = new ShowtimeBean();

		Connection con = DBConnection.getConnection();
		String sql = "SELECT * FROM showtime WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, showtimeId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				obj.setId(rs.getInt("id"));
				obj.setLocalStartHour(rs.getTime("start_hour").toLocalTime());
				obj.setLocalEndHour(rs.getTime("end_hour").toLocalTime());
				obj.setStartHour(rs.getTime("start_hour"));
				obj.setEndHour(rs.getTime("end_hour"));
				obj.setStatus(rs.getInt("status"));
			}

		} catch (SQLException e) {
			System.out.println("get showtime by id error : " + e.getMessage());
		}

		return obj;
	}

	public List<ShowtimeBean> getShowtimeByCinemaId(int cinemaId) {
		List<ShowtimeBean> showtimeList = new ArrayList<ShowtimeBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT s.* FROM showtime s JOIN cinema_showtime cs ON s.id = cs.showtime_id WHERE cs.cinema_id = ? AND s.status = ? ORDER BY s.start_hour;";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, cinemaId);
			ps.setInt(2, 1); // status - 1 - active

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ShowtimeBean obj = new ShowtimeBean();
				obj.setId(rs.getInt("id"));
				obj.setStartHour(rs.getTime("start_hour"));
				obj.setEndHour(rs.getTime("end_hour"));
				obj.setStatus(rs.getInt("status"));
				showtimeList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get showtime by cinema error : " + e.getMessage());
		}

		return showtimeList;
	}

	public int hardDeleteShowtime(int showtimeId) {
		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "DELETE FROM `showtime` WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, showtimeId);
			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("delete showtime error : " + e.getMessage());
		}

		return i;
	}
}
