package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.FeatureBean;
import com.spring.model.SeatTypeBean;

@Repository
public class FeatureRepository {
	
	
	public int addFeature (FeatureBean feature) {
		
		int i = 0;
		
		Connection con = DBConnection.getConnection();
		String sql = "INSERT INTO feature (name, description, status) VALUES (?, ?, ?);";
		
		try {
			
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, feature.getName());
			ps.setString(2, feature.getDescription());
			ps.setInt(3, feature.getStatus());
			
			i = ps.executeUpdate();
			
			} catch (Exception e) {
			
			System.out.println(" error :" + e.getMessage());
		}
		
		return i ;
	}
	
	public List<FeatureBean> showAllFeature() {
		List<FeatureBean> featureList = new ArrayList<FeatureBean>();

		Connection con = DBConnection.getConnection();
		String sql = "SELECT * FROM feature";

		try {
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				FeatureBean obj = new FeatureBean();
			    obj.setId(rs.getInt("id"));
			    obj.setName(rs.getString("name"));
			    obj.setDescription(rs.getString("description"));
			    obj.setStatus(rs.getInt("status"));
				
				featureList.add(obj);
				
			}

		} catch (SQLException e) {
			System.out.println("feature list :" + e.getMessage());
		}
		return featureList;
	}
	
	public List<FeatureBean> showAllActiveFeature() {
		List<FeatureBean> featureList = new ArrayList<FeatureBean>();

		Connection con = DBConnection.getConnection();
		String sql = "SELECT * FROM feature where status = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, 1);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {

				FeatureBean obj = new FeatureBean();
			    obj.setId(rs.getInt("id"));
			    obj.setName(rs.getString("name"));
			    obj.setDescription(rs.getString("description"));
			    obj.setStatus(rs.getInt("status"));
				
				featureList.add(obj);
				
			}

		} catch (SQLException e) {
			System.out.println("feature list :" + e.getMessage());
		}
		return featureList;
	}

	public int deleteFeature(int id) {

		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "delete from feature where id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);

			i = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("delete feature error: " + e.getMessage());
		}
		return i;
	}

	public int updateFeature(FeatureBean feature) {

		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "update feature set name = ?, description = ?, status = ? where id = ?;";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, feature.getName());
			ps.setString(2, feature.getDescription());
			ps.setInt(3, feature.getStatus());
			ps.setInt(4, feature.getId());
			
            i = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("update feature error: " + e.getMessage());
		}
		return i;
	}

	public FeatureBean showFeaturebyId(int id) {
		FeatureBean obj = null;

		Connection con = DBConnection.getConnection();
		String sql = "select * from feature where id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				obj = new FeatureBean();
                obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setDescription(rs.getString("description"));
				obj.setStatus(rs.getInt("status"));

			}

		}

		catch (SQLException e) {
			System.out.println("select feature error :" + e.getMessage());

		}

		return obj;

	}
	
	

}
