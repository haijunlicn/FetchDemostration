package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.spring.model.PaymentBean;

@Repository
public class PaymentRepository {
	
	public List<PaymentBean> showAllPayment(){
		List<PaymentBean> paylist=new ArrayList<>();
		
		Connection con=DBConnection.getConnection();
		String sql="select *from payment_method";
		
		try {
			PreparedStatement ps=con.prepareStatement(sql);
			ResultSet rs= ps.executeQuery();
			
			while (rs.next()) {
				PaymentBean pay=new PaymentBean();
				pay.setId(rs.getInt("id"));
				pay.setMethod_name(rs.getString("method_name"));
				pay.setAcc_num(rs.getString("acc_num"));
				pay.setQr_path(rs.getString("qr_path"));
				pay.setStatus(rs.getInt("status"));
				paylist.add(pay);
			}
		} catch (SQLException e) {
			System.out.println("payment error"+ e.getMessage());	
		}
		
		return paylist;
	}
	public int insertPayment(PaymentBean obj) {
		int i=0;
		
		Connection con=DBConnection.getConnection();
		String sql="insert into payment_method(method_name,acc_num,qr_path)values(?,?,?)";
		
		try {
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, obj.getMethod_name());
			ps.setString(2, obj.getAcc_num());
			ps.setString(3, obj.getQr_path());
			i=ps.executeUpdate();
		} catch (SQLException e) {
				System.out.println("payment insert" +e.getMessage());	
		}
		return i;	
	}
	public int deletePaymentById(int paymentId) {
		
		int i=0;
		
		Connection con=DBConnection.getConnection();
		String sql="delete from payment_method where id=?";
		
		try {
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1, paymentId);
			
			i=ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("delete payment error: " + e.getMessage());
		}
		return i;
	}
	public int updatePaymentById (PaymentBean obj) {
		
		int i=0;
		
		Connection con=DBConnection.getConnection();
		String sql="update payment_method set method_name=?, acc_num=?, qr_path=? where id=?";
		
		try {
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setString(1, obj.getMethod_name());
			ps.setString(2, obj.getAcc_num());
			ps.setString(3, obj.getQr_path());
			ps.setInt(4, obj.getId());
			i=ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("update payment error: " + e.getMessage());
		}
		return i;
	}
	public PaymentBean showPaymentbyId(int paymentid) {
		PaymentBean obj=null;
		
		Connection con=DBConnection.getConnection();
		String sql="select *from payment_method where id=?";
		
		
		try {
			PreparedStatement ps =con.prepareStatement(sql);
			ps.setInt(1, paymentid);
			
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				obj=new PaymentBean();
				obj.setId(rs.getInt("id"));
				obj.setMethod_name(rs.getString("method_name"));
				obj.setAcc_num(rs.getString("acc_num"));
				obj.setQr_path(rs.getString("qr_path"));
				obj.setStatus(rs.getInt("status"));				
			}
		} catch (SQLException e) {
			System.out.println("select one payment error :" +e.getMessage());
		}
		
		return obj;
	}
	public int softdeletePayment(int paymentId) {
		
		int i=0;
		
		Connection con=DBConnection.getConnection();
		String sql="update payment_method set status=? where id=?";
		
		try {
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1, paymentId);
			ps.setInt(2, 1);
			
			i=ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("softdelete: " + e.getMessage());
		}
		return i;
	}
}
