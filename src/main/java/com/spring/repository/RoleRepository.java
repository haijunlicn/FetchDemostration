package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.RoleBean;

@Repository
public class RoleRepository {
	public List<RoleBean> getAllRole() {
		List<RoleBean> roleList = new ArrayList<RoleBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT * FROM `role`";

		try {
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				RoleBean obj = new RoleBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("role_name"));
				roleList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get role error : " + e.getMessage());
		}

		return roleList;
	}
}
