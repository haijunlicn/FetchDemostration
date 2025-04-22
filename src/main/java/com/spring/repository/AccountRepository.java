package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import com.spring.model.AccountBean;
import com.spring.model.ShowtimeBean;

@Repository
public class AccountRepository {

	public AccountBean loginAccount(AccountBean user) {
		AccountBean obj = null;
		Connection con = DBConnection.getConnection();

		/*
		 * String sql = "SELECT a.*, c.name AS cinema_name " +
		 * "FROM `account` a LEFT JOIN `cinema` c " + "ON a.assigned_cinema_id = c.id "
		 * + "WHERE a.email = ? AND (a.role_id = ? OR ? <> 3)";
		 */

		String sql = "SELECT a.*, c.name AS cinema_name " + "FROM `account` a LEFT JOIN `cinema` c "
				+ "ON a.assigned_cinema_id = c.id " + "WHERE a.email = ? AND a.role_id <> ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getEmail());
			ps.setInt(2, 3);
			// ps.setInt(3, user.getRoleId());

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				obj = new AccountBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setEmail(rs.getString("email"));
				obj.setPassword(rs.getString("password"));
				obj.setProfilePath(rs.getString("profile_path"));
				obj.setRoleId(rs.getInt("role_id"));
				obj.setAssignedCinemaId(rs.getInt("assigned_cinema_id"));
				obj.setAssignedCinemaName(rs.getString("cinema_name"));
				obj.setStatus(rs.getInt("status"));
				obj.setCreatedDate(rs.getTimestamp("created_date"));
				obj.setUpdatedDate(rs.getTimestamp("updated_date"));
			}

		} catch (SQLException e) {
			System.out.println("admin login error : " + e.getMessage());
		}

		return obj;
	}

	public AccountBean loginCusAccount(AccountBean user) {
		AccountBean obj = null;
		Connection con = DBConnection.getConnection();

		String sql = "SELECT a.*, c.name AS cinema_name " + "FROM `account` a LEFT JOIN `cinema` c "
				+ "ON a.assigned_cinema_id = c.id " + "WHERE a.email = ? AND (a.role_id = ?)";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, user.getEmail());
			ps.setInt(2, user.getRoleId());
			/* ps.setInt(3, user.getRoleId()); */

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				obj = new AccountBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setEmail(rs.getString("email"));
				obj.setPassword(rs.getString("password"));
				obj.setProfilePath(rs.getString("profile_path"));
				obj.setRoleId(rs.getInt("role_id"));
				obj.setAssignedCinemaId(rs.getInt("assigned_cinema_id"));
				obj.setAssignedCinemaName(rs.getString("cinema_name"));
				obj.setStatus(rs.getInt("status"));
				obj.setCreatedDate(rs.getTimestamp("created_date"));
				obj.setUpdatedDate(rs.getTimestamp("updated_date"));
			}

		} catch (SQLException e) {
			System.out.println("admin login error : " + e.getMessage());
		}

		return obj;
	}

	public List<AccountBean> getAllCinemaAdmins() {
		List<AccountBean> cinemaAdminList = new ArrayList<AccountBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT a.*, c.name AS cinema_name "
				+ "FROM account a LEFT JOIN cinema c ON a.assigned_cinema_id = c.id "
				+ "WHERE a.role_id = ? ORDER BY a.created_date DESC;";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, 2); // role_id 2 is cinema admin

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				AccountBean obj = new AccountBean();
				obj = new AccountBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setEmail(rs.getString("email"));
				obj.setPassword(rs.getString("password"));
				obj.setProfilePath(rs.getString("profile_path"));
				obj.setRoleId(rs.getInt("role_id"));
				obj.setAssignedCinemaId(rs.getInt("assigned_cinema_id"));
				obj.setAssignedCinemaName(rs.getString("cinema_name"));
				obj.setStatus(rs.getInt("status"));
				obj.setCreatedDate(rs.getTimestamp("created_date"));
				obj.setUpdatedDate(rs.getTimestamp("updated_date"));

				cinemaAdminList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get cinema admin error : " + e.getMessage());
		}

		return cinemaAdminList;
	}

	public boolean checkEmailDuplicate(String email, Integer accId) {
		boolean result = false;

		Connection con = DBConnection.getConnection();
		String sql = "SELECT * FROM `account` WHERE email = ? AND role_id = ?";

		// If updating, exclude the current account
		if (accId != null) {
			sql += " AND id <> ?";
		}

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, email);
			ps.setInt(2, 2); // role_id 2 is cinema admin
			if (accId != null) {
				ps.setInt(3, accId);
			}
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				result = true;
			}

		} catch (SQLException e) {
			System.out.println("check email dup error : " + e.getMessage());
		}

		return result;
	}

	public AccountBean getAccountByAccId(int accId) {
		AccountBean obj = null;

		Connection con = DBConnection.getConnection();
		String sql = "SELECT a.*, c.name AS cinema_name " + "FROM `account` a LEFT JOIN `cinema` c "
				+ "ON a.assigned_cinema_id = c.id WHERE a.id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, accId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				obj = new AccountBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setEmail(rs.getString("email"));
				obj.setPassword(rs.getString("password"));
				obj.setProfilePath(rs.getString("profile_path"));
				obj.setRoleId(rs.getInt("role_id"));
				obj.setAssignedCinemaId(rs.getInt("assigned_cinema_id"));
				obj.setAssignedCinemaName(rs.getString("cinema_name"));
				obj.setStatus(rs.getInt("status"));
				obj.setCreatedDate(rs.getTimestamp("created_date"));
				obj.setUpdatedDate(rs.getTimestamp("updated_date"));
			}

		} catch (SQLException e) {
			System.out.println("getAccountByAccId error : " + e.getMessage());
		}

		return obj;
	}

	public List<AccountBean> getAccsByCinemaId(int cinemaId) {
		List<AccountBean> accList = new ArrayList<AccountBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT * FROM account WHERE assigned_cinema_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, cinemaId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				AccountBean obj = new AccountBean();
				obj = new AccountBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setEmail(rs.getString("email"));
				obj.setPassword(rs.getString("password"));
				obj.setProfilePath(rs.getString("profile_path"));
				obj.setRoleId(rs.getInt("role_id"));
				obj.setAssignedCinemaId(rs.getInt("assigned_cinema_id"));
				/* obj.setAssignedCinemaName(rs.getString("cinema_name")); */
				obj.setStatus(rs.getInt("status"));
				obj.setCreatedDate(rs.getTimestamp("created_date"));
				obj.setUpdatedDate(rs.getTimestamp("updated_date"));

				accList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get acc by cinema id error : " + e.getMessage());
		}

		return accList;
	}

	public int addNewCinemaAdmin(AccountBean accObj) {
		int i = -1;
		Connection con = DBConnection.getConnection();
		String sql = "INSERT INTO `account`(`name`, `email`, `password`, `profile_path`, `role_Id`, `assigned_cinema_id`, `status`) VALUES (?,?,?,?,?,?,?)";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			String hashedPassword = BCrypt.hashpw(accObj.getPassword(), BCrypt.gensalt());
			ps.setString(1, accObj.getName());
			ps.setString(2, accObj.getEmail());
			ps.setString(3, hashedPassword);
			ps.setString(4, accObj.getProfilePath());
			ps.setInt(5, 2); // role_id is 2 for cinema admin
			if (accObj.getAssignedCinemaId() != null) {
				ps.setInt(6, accObj.getAssignedCinemaId());
			} else {
				ps.setNull(6, java.sql.Types.INTEGER);
			}
			ps.setInt(7, accObj.getStatus());
			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("add cinema admin error: " + e.getMessage());
		}

		return i;
	}

	public int updateAccount(AccountBean accObj) {
		int i = -1;
		Connection con = DBConnection.getConnection();
		String sql = "UPDATE `account` SET `name`= ?, `email`= ?, `profile_path`= ?, `assigned_cinema_id`= ?, `status`= ? WHERE id = ?;";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, accObj.getName());
			ps.setString(2, accObj.getEmail());
			ps.setString(3, accObj.getProfilePath());
			if (accObj.getAssignedCinemaId() != null) {
				ps.setInt(4, accObj.getAssignedCinemaId());
			} else {
				ps.setNull(4, java.sql.Types.INTEGER);
			}
			ps.setInt(5, accObj.getStatus());
			ps.setInt(6, accObj.getId());
			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Update account error: " + e.getMessage());
		}

		return i;
	}

	public int hardDeleteAccount(int accId) {
		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "DELETE FROM `account` WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, accId);
			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("delete account error : " + e.getMessage());
		}

		return i;
	}

	public void save(AccountBean customer) {
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = null;

		try {
			String sql = "INSERT INTO account (name, email, password, role_Id) " + "VALUES (?, ?, ?, ?)";
			ps = con.prepareStatement(sql);

			ps.setString(1, customer.getName());
			ps.setString(2, customer.getEmail());
			ps.setString(3, customer.getPassword());
			ps.setInt(4, 3);

			int rowsAffected = ps.executeUpdate();
			System.out.println("Rows inserted: " + rowsAffected); // Log rows affected
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(con, ps, null);
		}
	}

	public AccountBean findByEmail(String email) {
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		AccountBean customer = null;

		try {
			String sql = "SELECT * FROM account WHERE email = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, email);
			rs = ps.executeQuery();

			if (rs.next()) {
				customer = new AccountBean();
				customer.setId(rs.getInt("id"));
				customer.setName(rs.getString("name"));
				customer.setEmail(rs.getString("email"));
				customer.setPassword(rs.getString("password"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(con, ps, rs);
		}

		return customer;
	}

	private void closeResources(Connection con, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (ps != null)
				ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean existsByEmail(String email) {
		Connection con = DBConnection.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean exists = false;

		try {
			String sql = "SELECT COUNT(*) FROM account WHERE email = ? and role_id = ?";
			ps = con.prepareStatement(sql);
			ps.setString(1, email);
			ps.setInt(2, 3); // role_id 3 is customer
			rs = ps.executeQuery();

			if (rs.next()) {
				exists = rs.getInt(1) > 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResources(con, ps, rs);
		}

		return exists;
	}

	public List<AccountBean> getAllCustomer() {
		List<AccountBean> CustomerList = new ArrayList<AccountBean>();

		Connection con = DBConnection.getConnection();

		String sql = "select *from account where role_Id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, 3);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				AccountBean obj = new AccountBean();
				obj = new AccountBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setEmail(rs.getString("email"));
				obj.setPassword(rs.getString("password"));
				obj.setRoleId(rs.getInt("role_id"));
				obj.setAssignedCinemaId(rs.getInt("assigned_cinema_id"));
				obj.setStatus(rs.getInt("status"));
//				obj.setCreated_date(rs.getTimestamp("created_date"));
//				obj.setUpdated_date(rs.getTimestamp("updated_date"));

				CustomerList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get customer error : " + e.getMessage());
		}

		return CustomerList;
	}

	public AccountBean getCustomerByAccId(int accId) {
		AccountBean obj = null;

		Connection con = DBConnection.getConnection();
		String sql = "select *from account where role_Id=?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, accId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				obj = new AccountBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setEmail(rs.getString("email"));
				obj.setPassword(rs.getString("password"));
				obj.setRoleId(rs.getInt("role_id"));
				obj.setAssignedCinemaId(rs.getInt("assigned_cinema_id"));
				obj.setStatus(rs.getInt("status"));
			}

		} catch (SQLException e) {
			System.out.println("getAccountByAccId error : " + e.getMessage());
		}

		return obj;
	}

	public int softDeleteCustomerById(int customerId) {

		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "UPDATE account SET status = 0 WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, customerId);

			i = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("soft delete customer error: " + e.getMessage());
		}
		return i;
	}

	public int toggleCustomerStatus(int customerId) {
		int result = 0;
		Connection con = DBConnection.getConnection();

		String sql = "UPDATE account SET status = CASE WHEN status = 1 THEN 0 ELSE 1 END WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, customerId);

			result = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("toggle customer status error: " + e.getMessage());
		}
		return result;
	}

}
