package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.StateBean;

@Repository
public class StateRepository {

	public List<StateBean> showAllState() {
		List<StateBean> statelist = new ArrayList<>();

		Connection con = DBConnection.getConnection();

		String sql = "select * from state";

		try {
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				StateBean obj = new StateBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setStatus(rs.getInt("status"));
				statelist.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get all state error : " + e.getMessage());
		}

		return statelist;
	}
	
	public List<StateBean> showAllActiveState() {
		List<StateBean> statelist = new ArrayList<>();

		Connection con = DBConnection.getConnection();

		String sql = "select * from state where status = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, 1);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				StateBean obj = new StateBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setStatus(rs.getInt("status"));
				statelist.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get all state error : " + e.getMessage());
		}

		return statelist;
	}

	public int insertState(StateBean obj) {
		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "insert into state(name, status) values(?, ?)";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getStatus());

			i = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("state insert" + e.getMessage());
		}
		return i;
	}

	public int deleteStateById(int stateId) {

		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "delete from state where id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, stateId);

			i = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("delete state error: " + e.getMessage());
		}
		return i;
	}

	public int updateStateById(StateBean obj) {

		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "update state set name=?, status=? where id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getStatus());
			ps.setInt(3, obj.getId());

			i = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("update state error: " + e.getMessage());
		}
		return i;
	}

	public StateBean showStatebyId(int stateid) {
		StateBean obj = null;

		Connection con = DBConnection.getConnection();
		String sql = "select *from state where id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, stateid);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				obj = new StateBean();

				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setStatus(rs.getInt("status"));

			}
		} catch (SQLException e) {
			System.out.println("select one state error :" + e.getMessage());
		}

		return obj;
	}

	public int softdeleteState(int stateId) {

		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "update state set status=? where id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, stateId);
			ps.setInt(2, 1);

			i = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("softdelete: " + e.getMessage());
		}
		return i;
	}

}
