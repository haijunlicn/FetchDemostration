package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.CastBean;

@Repository
public class CastRepository {

	public List<CastBean> showAllCast() {
		List<CastBean> castlist = new ArrayList<>();

		Connection con = DBConnection.getConnection();
		String sql = "select * from cast";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CastBean cast = new CastBean();
				cast.setId(rs.getInt("id"));
				cast.setName(rs.getString("name"));
				cast.setBirthdate(rs.getDate("birthdate"));
				cast.setNationality(rs.getString("nationality"));
				cast.setGender(rs.getString("gender"));
				cast.setProfilePath(rs.getString("profile_path"));
				cast.setBiography(rs.getString("biography"));
				castlist.add(cast);
			}
		} catch (SQLException e) {
			System.out.println("Cast list error" + e.getMessage());
		}
		return castlist;
	}

	public int insertCast(CastBean castObj) {
		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "INSERT INTO cast (name, birthdate, nationality, biography, gender, profile_path) VALUES (?, ?, ?, ?, ?, ?)";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, castObj.getName());
			ps.setDate(2, java.sql.Date.valueOf(castObj.getLocalBirthdate()));
			ps.setString(3, castObj.getNationality());
			ps.setString(4, castObj.getBiography());
			ps.setString(5, castObj.getGender());
			ps.setString(6, castObj.getProfilePath());
			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("insert cast error: " + e.getMessage());
		}
		return i;
	}

	public int deleteCastById(int castId) {
		int i = 0;
		Connection con = DBConnection.getConnection();
		String sql = "delete from cast where id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, castId);

			i = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("delete cast error: " + e.getMessage());
		}
		return i;
	}

	public int updateCastById(CastBean obj) {
		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "UPDATE cast SET name = ?, birthdate = ?, nationality = ?, gender = ?, biography = ?, profile_path = ? WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, obj.getName());
			ps.setDate(2, java.sql.Date.valueOf(obj.getLocalBirthdate()));
			ps.setString(3, obj.getNationality());
			ps.setString(4, obj.getGender());
			ps.setString(5, obj.getBiography());
			ps.setString(6, obj.getProfilePath());
			ps.setInt(7, obj.getId());

			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("update cast error: " + e.getMessage());
		}
		return i;
	}

	public CastBean showCastbyId(int castid) {
		CastBean obj = null;

		Connection con = DBConnection.getConnection();
		String sql = "select *from cast where id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, castid);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				obj = new CastBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setBirthdate(rs.getDate("birthdate"));
				obj.setLocalBirthdate(rs.getDate("birthdate").toLocalDate());
				obj.setNationality(rs.getString("nationality"));
				obj.setBiography(rs.getString("biography"));
				obj.setGender(rs.getString("gender"));
				obj.setProfilePath(rs.getString("profile_path"));
			}
		} catch (SQLException e) {
			System.out.println("select one cast error :" + e.getMessage());
		}
		return obj;
	}
}
