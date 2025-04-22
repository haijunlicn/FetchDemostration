
package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import com.spring.model.SeatTypeBean;

@Repository
public class SeatTypeRepository {

	public int addSeat(SeatTypeBean addseat) {

		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "insert into seat_type (seat_type,size,status) values (?, ?, ?)";

		try {

			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, addseat.getSeatTypeName());
			ps.setDouble(2, addseat.getSize());
			ps.setInt(3, 1);

			i = ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(" error :" + e.getMessage());
		}

		return i;
	}

	public List<SeatTypeBean> showAllSeatType() {
		List<SeatTypeBean> seatTypeList = new ArrayList<SeatTypeBean>();

		Connection con = DBConnection.getConnection();
		String sql = "SELECT * FROM seat_type;";

		try {
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				SeatTypeBean obj = new SeatTypeBean();
				obj.setId(rs.getInt("id"));
				obj.setSeatTypeName(rs.getString("seat_type"));
				obj.setSize(rs.getDouble("size"));
				obj.setStatus(rs.getInt("status"));
				seatTypeList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("seat type list :" + e.getMessage());
		}
		return seatTypeList;
	}

	public int deleteSeatType(int id) {

		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "delete from seat_type where id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);

			i = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("delete seat type error: " + e.getMessage());
		}
		return i;
	}

	public int updateSeatType(SeatTypeBean seatType) {

		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "update seat_type set seat_type = ?, size = ? where id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, seatType.getSeatTypeName());
			ps.setDouble(2, seatType.getSize());
			ps.setInt(3, seatType.getId());

			i = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("update seat type error: " + e.getMessage());
		}
		return i;
	}

	public SeatTypeBean showSeatTypebyId(int id) {
		SeatTypeBean obj = null;

		Connection con = DBConnection.getConnection();
		String sql = "select *from seat_type where id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				obj = new SeatTypeBean();

				obj.setId(rs.getInt("id"));
				obj.setSeatTypeName(rs.getString("seat_type"));
				obj.setSize(rs.getDouble("size"));

			}

		}

		catch (SQLException e) {
			System.out.println("select seat type error :" + e.getMessage());

		}

		return obj;

	}
}
