package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.CategoryBean;

@Repository
public class CategoryRepository {
	
	public List<CategoryBean> showAllCategory(){
		List<CategoryBean> catelist=new ArrayList<CategoryBean>();
		
		Connection con=DBConnection.getConnection();
		String sql="select *from category";
		
		try {
			PreparedStatement ps=con.prepareStatement(sql);
			ResultSet rs= ps.executeQuery();
			
			while (rs.next()) {
				CategoryBean cate=new CategoryBean();
				cate.setId(rs.getInt("id"));
				cate.setName(rs.getString("name"));
				
				catelist.add(cate);
			}
		} catch (SQLException e) {
			System.out.println("movie category error"+ e.getMessage());	
		}
		
		return catelist;
	}
	
	public int insertCategory(String name) {
		int i=0;
		
		Connection con=DBConnection.getConnection();
		String sql="insert into category(name)value(?)";
		
		try {
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, name);
			
			i=ps.executeUpdate();
			
			
		} catch (SQLException e) {
				System.out.println("movie category insert" +e.getMessage());	
		}
		return i;	
	}
	public int deleteCategoryById(int categoryId) {
		
		int i=0;
		
		Connection con=DBConnection.getConnection();
		String sql="delete from category where id=?";
		
		try {
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1, categoryId);
			
			i=ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("delete category error: " + e.getMessage());
		}
		return i;
	}
	public int updateCatgoryById (CategoryBean obj) {
		
		int i=0;
		
		Connection con=DBConnection.getConnection();
		String sql="update category set name=? where id=?";
		
		try {
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, obj.getName());
			ps.setInt(2, obj.getId());
			
			i=ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("update category error: " + e.getMessage());
		}
		return i;
	}
	
	public CategoryBean showCategorybyId(int categoryid) {
		CategoryBean obj=null;
		
		Connection con=DBConnection.getConnection();
		String sql="select * from category where id=?";
		
		
		try {
			PreparedStatement ps =con.prepareStatement(sql);
			ps.setInt(1, categoryid);
			
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				obj=new CategoryBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				
			}
		} catch (SQLException e) {
			System.out.println("select one category error :" +e.getMessage());
		}
		
		
		return obj;
	}
	
}
