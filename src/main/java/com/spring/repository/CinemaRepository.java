package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.model.CategoryBean;
import com.spring.model.CinemaBean;
import com.spring.model.CinemaImageBean;
import com.spring.model.FeatureBean;
import com.spring.model.ShowtimeBean;

@Repository
public class CinemaRepository {

	@Autowired
	SeatRepository seatRepo;

	@Autowired
	FeatureRepository featureRepo;

	public List<CinemaBean> getAllCinema() {
		List<CinemaBean> cinemaList = new ArrayList<>();
		String sql = "SELECT c.*, t.name AS township_name, s.id AS state_id, s.name AS state_name " + "FROM cinema c "
				+ "JOIN township t ON c.township_id = t.id " + "JOIN state s ON t.state_id = s.id "
				+ "ORDER BY c.created_date DESC";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				cinemaList.add(mapResultSetToCinemaBean(rs));
			}

		} catch (SQLException e) {
			System.out.println("get cinema error : " + e.getMessage());
		}

		return cinemaList;
	}

	public List<CinemaBean> getActiveCinema() {
		List<CinemaBean> cinemaList = new ArrayList<>();
		String sql = "SELECT c.*, t.name AS township_name, s.id AS state_id, s.name AS state_name " + "FROM cinema c "
				+ "JOIN township t ON c.township_id = t.id " + "JOIN state s ON t.state_id = s.id "
				+ "WHERE c.status = 1 " + "ORDER BY c.created_date DESC";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				cinemaList.add(mapResultSetToCinemaBean(rs));
			}

		} catch (SQLException e) {
			System.out.println("get active cinema error : " + e.getMessage());
		}

		return cinemaList;
	}

	public List<CinemaBean> getCinemasPaginated(int page, int size) {
		List<CinemaBean> cinemaList = new ArrayList<>();
		int offset = (page - 1) * size;

		String sql = "SELECT c.*, t.name AS township_name, s.id AS state_id, s.name AS state_name " + "FROM cinema c "
				+ "JOIN township t ON c.township_id = t.id " + "JOIN state s ON t.state_id = s.id "
				+ "ORDER BY c.created_date DESC " + "LIMIT ? OFFSET ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, size);
			ps.setInt(2, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					cinemaList.add(mapResultSetToCinemaBean(rs));
				}
			}

		} catch (SQLException e) {
			System.out.println("get cinema error : " + e.getMessage());
		}

		return cinemaList;
	}

	private CinemaBean mapResultSetToCinemaBean(ResultSet rs) throws SQLException {
		CinemaBean obj = new CinemaBean();
		obj.setId(rs.getInt("id"));
		obj.setName(rs.getString("name"));
		obj.setTownshipId(rs.getInt("township_id"));
		obj.setTownshipName(rs.getString("township_name"));
		obj.setStateId(rs.getInt("state_id"));
		obj.setStateName(rs.getString("state_name"));
		obj.setTotalSeats(rs.getInt("total_seats"));
		obj.setRowCount(rs.getInt("row_count"));
		obj.setColCount(rs.getInt("col_count"));
		obj.setLat(rs.getString("lat"));
		obj.setLon(rs.getString("long"));
		obj.setStatus(rs.getInt("status"));
		obj.setCreatedDate(rs.getTimestamp("created_date"));
		obj.setUpdatedDate(rs.getTimestamp("updated_date"));
		obj.setSeatList(seatRepo.getSeatsByCinemaId(rs.getInt("id")));
		obj.setSelectedFeatureList(getFeatureByCinemaId(rs.getInt("id")));
		obj.setUploadedImgs(getImgsByCinemaId(obj.getId()));
		return obj;
	}

	public int getTotalCinemaCount() {
		int cinemaCount = -1;
		String sql = "SELECT COUNT(*) FROM cinema";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				cinemaCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("get cinema count : " + e.getMessage());
		}

		return cinemaCount;
	}

	public CinemaBean getCinemaById(int cinemaId) {
		CinemaBean obj = new CinemaBean();

		Connection con = DBConnection.getConnection();
		String sql = "SELECT c.*, t.name AS township_name, s.id AS state_id, s.name AS state_name FROM cinema c "
				+ "JOIN township t ON c.township_id = t.id JOIN state s ON t.state_id = s.id WHERE c.id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, cinemaId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				obj = mapResultSetToCinemaBean(rs);
			}

		} catch (SQLException e) {
			System.out.println("get cinema by id error : " + e.getMessage());
		}

		return obj;
	}
	
	public String getCinemaNameById(int cinemaId) {
	    String cinemaName = "";

	    String sql = "SELECT name FROM cinema WHERE id = ?";

	    try (Connection con = DBConnection.getConnection(); 
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        
	        ps.setInt(1, cinemaId);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            cinemaName = rs.getString("name");
	        }

	    } catch (SQLException e) {
	        System.out.println("get cinemaName by id error: " + e.getMessage());
	    }

	    return cinemaName;
	}

	public int addNewCinema(CinemaBean cinemaObj) {
		int i = -1;

		Connection con = DBConnection.getConnection();

		String sql = "INSERT INTO `cinema`(`name`, `township_id`, `total_seats`, "
				+ "`row_count`, `col_count`, `lat`, `long`, `status`) VALUES (?,?,?,?,?,?,?,?)";

		try {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, cinemaObj.getName());
			ps.setInt(2, cinemaObj.getTownshipId());
			ps.setInt(3, cinemaObj.getTotalSeats());
			ps.setInt(4, cinemaObj.getRowCount());
			ps.setInt(5, cinemaObj.getColCount());
			ps.setString(6, cinemaObj.getLat());
			ps.setString(7, cinemaObj.getLon());
			ps.setInt(8, cinemaObj.getStatus());
			i = ps.executeUpdate();

			if (i > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						int generatedId = rs.getInt(1);
						return generatedId;
					}
				}
			} else {
				System.out.println("No cinema inserted.");
			}
		} catch (SQLException e) {
			System.out.println("add cinema error : " + e.getMessage());
		}

		return i;
	}

	public int updateCinema(CinemaBean cinemaObj) {
		int i = -1;

		Connection con = DBConnection.getConnection();

		String sql = "UPDATE `cinema` SET `name` = ?, `township_id` = ?, `total_seats` = ?, "
				+ "`row_count` = ?, `col_count` = ?, `lat` = ?, `long` = ?, `status` = ? WHERE `id` = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, cinemaObj.getName());
			ps.setInt(2, cinemaObj.getTownshipId());
			ps.setInt(3, cinemaObj.getTotalSeats());
			ps.setInt(4, cinemaObj.getRowCount());
			ps.setInt(5, cinemaObj.getColCount());
			ps.setString(6, cinemaObj.getLat());
			ps.setString(7, cinemaObj.getLon());
			ps.setInt(8, cinemaObj.getStatus());
			ps.setInt(9, cinemaObj.getId());
			i = ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println("update cinema error : " + e.getMessage());
		}

		return i;
	}

	public void updateCinemaImages(int cinemaId, List<String> imgUrlList, List<Integer> toDeleteImgIdList) {
		Connection con = DBConnection.getConnection();

		// SQL for inserting new images
		String insertSql = "INSERT INTO cinema_image (cinema_id, img_url) VALUES (?, ?)";

		try {
			if (!toDeleteImgIdList.isEmpty()) {
				StringBuilder deleteSql = new StringBuilder("DELETE FROM cinema_image WHERE id IN (");
				deleteSql.append("?,".repeat(toDeleteImgIdList.size()));
				deleteSql.setLength(deleteSql.length() - 1);
				deleteSql.append(")");

				PreparedStatement deletePs = con.prepareStatement(deleteSql.toString());
				for (int i = 0; i < toDeleteImgIdList.size(); i++) {
					deletePs.setInt(i + 1, toDeleteImgIdList.get(i));
				}
				deletePs.executeUpdate();
				deletePs.close();
			}

			// Insert new rows
			if (!imgUrlList.isEmpty()) { // Only insert if there are new images
				PreparedStatement insertPs = con.prepareStatement(insertSql);
				for (String imgUrl : imgUrlList) {
					insertPs.setInt(1, cinemaId);
					insertPs.setString(2, imgUrl);
					insertPs.addBatch();
				}
				insertPs.executeBatch();
				insertPs.close();
			}

		} catch (SQLException e) {
			System.out.println("Error updating cinema images: " + e.getMessage());
		}
	}

	public void updateCinemaShowtimes(int cinemaId, List<Integer> showtimeIds) {
		Connection con = DBConnection.getConnection();

		// SQL for inserting new showtimes that don't already exist
		String insertSql = "INSERT INTO cinema_showtime (cinema_id, showtime_id) "
				+ "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM cinema_showtime WHERE cinema_id = ? AND showtime_id = ?)";

		try {
			if (!showtimeIds.isEmpty()) {
				StringBuilder deleteSql = new StringBuilder(
						"DELETE FROM cinema_showtime WHERE cinema_id = ? AND showtime_id NOT IN (");
				deleteSql.append("?,".repeat(showtimeIds.size()));
				deleteSql.setLength(deleteSql.length() - 1);
				deleteSql.append(")");

				PreparedStatement deletePs = con.prepareStatement(deleteSql.toString());
				deletePs.setInt(1, cinemaId);
				for (int i = 0; i < showtimeIds.size(); i++) {
					deletePs.setInt(i + 2, showtimeIds.get(i));
				}
				deletePs.executeUpdate();
				deletePs.close();
			}

			// Insert new showtimes that don't already exist
			PreparedStatement insertPs = con.prepareStatement(insertSql);
			for (int showtimeId : showtimeIds) {
				insertPs.setInt(1, cinemaId);
				insertPs.setInt(2, showtimeId);
				insertPs.setInt(3, cinemaId);
				insertPs.setInt(4, showtimeId);
				insertPs.addBatch();
			}
			insertPs.executeBatch();
			insertPs.close();

		} catch (SQLException e) {
			System.out.println("Error updating cinema showtimes: " + e.getMessage());
		}
	}

	public void updateCinemaFeatures(int cinemaId, List<Integer> featureIds) {
		Connection con = DBConnection.getConnection();

		// SQL for inserting new features that don't already exist
		String insertSql = "INSERT INTO cinema_feature (cinema_id, feature_id) "
				+ "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM cinema_feature WHERE cinema_id = ? AND feature_id = ?)";

		try {
			if (!featureIds.isEmpty()) {
				StringBuilder deleteSql = new StringBuilder(
						"DELETE FROM cinema_feature WHERE cinema_id = ? AND feature_id NOT IN (");
				deleteSql.append("?,".repeat(featureIds.size()));
				deleteSql.setLength(deleteSql.length() - 1);
				deleteSql.append(")");

				PreparedStatement deletePs = con.prepareStatement(deleteSql.toString());
				deletePs.setInt(1, cinemaId);
				for (int i = 0; i < featureIds.size(); i++) {
					deletePs.setInt(i + 2, featureIds.get(i));
				}
				deletePs.executeUpdate();
				deletePs.close();
			}

			// Insert new features that don't already exist
			PreparedStatement insertPs = con.prepareStatement(insertSql);
			for (int featureId : featureIds) {
				insertPs.setInt(1, cinemaId);
				insertPs.setInt(2, featureId);
				insertPs.setInt(3, cinemaId);
				insertPs.setInt(4, featureId);
				insertPs.addBatch();
			}
			insertPs.executeBatch();
			insertPs.close();

		} catch (SQLException e) {
			System.out.println("Error updating cinema features: " + e.getMessage());
		}
	}

	/*
	 * public void updateCinemaImages(int cinemaId, List<String> imgUrlList,
	 * List<Integer> toDeleteImgIdList) { Connection con =
	 * DBConnection.getConnection();
	 * 
	 * // SQL for inserting new images String insertSql =
	 * "INSERT INTO cinema_image (cinema_id, img_url) VALUES (?, ?)";
	 * 
	 * try { if (!toDeleteImgIdList.isEmpty()) { StringBuilder deleteSql = new
	 * StringBuilder("DELETE FROM cinema_image WHERE id IN (");
	 * deleteSql.append("?,".repeat(toDeleteImgIdList.size()));
	 * deleteSql.setLength(deleteSql.length() - 1); deleteSql.append(")");
	 * 
	 * PreparedStatement deletePs = con.prepareStatement(deleteSql.toString()); for
	 * (int i = 0; i < toDeleteImgIdList.size(); i++) { deletePs.setInt(i + 1,
	 * toDeleteImgIdList.get(i)); } deletePs.executeUpdate(); deletePs.close(); }
	 * 
	 * // Insert new rows if (!imgUrlList.isEmpty()) { // Only insert if there are
	 * new images PreparedStatement insertPs = con.prepareStatement(insertSql); for
	 * (String imgUrl : imgUrlList) { insertPs.setInt(1, cinemaId);
	 * insertPs.setString(2, imgUrl); insertPs.addBatch(); }
	 * insertPs.executeBatch(); insertPs.close(); }
	 * 
	 * } catch (SQLException e) {
	 * System.out.println("Error updating cinema images: " + e.getMessage()); } }
	 * 
	 * public void updateCinemaShowtimes(int cinemaId, List<Integer> showtimeIds) {
	 * Connection con = DBConnection.getConnection();
	 * 
	 * // SQL for inserting new showtimes that don't already exist String insertSql
	 * = "INSERT INTO cinema_showtime (cinema_id, showtime_id) " +
	 * "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM cinema_showtime WHERE cinema_id = ? AND showtime_id = ?)"
	 * ;
	 * 
	 * // SQL for deleting showtimes that are not in the provided list String
	 * deleteSql =
	 * "DELETE FROM cinema_showtime WHERE cinema_id = ? AND showtime_id NOT IN (?)";
	 * 
	 * try { // Convert the showtimeIds list to a comma-separated string for the SQL
	 * query String showtimeIdsStr =
	 * showtimeIds.stream().map(String::valueOf).collect(Collectors.joining(","));
	 * 
	 * // Delete showtimes that are not in the provided list PreparedStatement
	 * deletePs = con.prepareStatement(deleteSql); deletePs.setInt(1, cinemaId); //
	 * Set cinemaId deletePs.setString(2, showtimeIdsStr); // Set the
	 * comma-separated list of showtimeIds deletePs.executeUpdate(); // Execute
	 * delete query
	 * 
	 * // Insert new showtimes that don't already exist PreparedStatement insertPs =
	 * con.prepareStatement(insertSql); for (int showtimeId : showtimeIds) {
	 * insertPs.setInt(1, cinemaId); insertPs.setInt(2, showtimeId);
	 * insertPs.setInt(3, cinemaId); // Set cinemaId for the check
	 * insertPs.setInt(4, showtimeId); // Set showtimeId for the check
	 * insertPs.addBatch(); // Add to batch } insertPs.executeBatch(); // Execute
	 * batch insert
	 * 
	 * } catch (SQLException e) {
	 * System.out.println("Error updating cinema showtimes: " + e.getMessage()); } }
	 * 
	 * public void updateCinemaFeatures(int cinemaId, List<Integer> featureIds) {
	 * Connection con = DBConnection.getConnection();
	 * 
	 * // SQL for inserting new showtimes that don't already exist String insertSql
	 * = "INSERT INTO cinema_feature (cinema_id, feature_id) " +
	 * "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM cinema_feature WHERE cinema_id = ? AND feature_id = ?)"
	 * ;
	 * 
	 * // SQL for deleting showtimes that are not in the provided list String
	 * deleteSql =
	 * "DELETE FROM cinema_feature WHERE cinema_id = ? AND feature_id NOT IN (?)";
	 * 
	 * try { // Convert the showtimeIds list to a comma-separated string for the SQL
	 * query String featureIdsStr =
	 * featureIds.stream().map(String::valueOf).collect(Collectors.joining(","));
	 * 
	 * // Delete showtimes that are not in the provided list PreparedStatement
	 * deletePs = con.prepareStatement(deleteSql); deletePs.setInt(1, cinemaId); //
	 * Set cinemaId deletePs.setString(2, featureIdsStr); // Set the comma-separated
	 * list of showtimeIds deletePs.executeUpdate(); // Execute delete query
	 * 
	 * // Insert new showtimes that don't already exist PreparedStatement insertPs =
	 * con.prepareStatement(insertSql); for (int featureId : featureIds) {
	 * insertPs.setInt(1, cinemaId); insertPs.setInt(2, featureId);
	 * insertPs.setInt(3, cinemaId); // Set cinemaId for the check
	 * insertPs.setInt(4, featureId); // Set showtimeId for the check
	 * insertPs.addBatch(); // Add to batch } insertPs.executeBatch(); // Execute
	 * batch insert
	 * 
	 * } catch (SQLException e) {
	 * System.out.println("Error updating cinema features: " + e.getMessage()); } }
	 */

	public List<CinemaImageBean> getImgsByCinemaId(int cinemaId) {
		List<CinemaImageBean> imgList = new ArrayList<CinemaImageBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT * FROM cinema_image WHERE cinema_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, cinemaId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CinemaImageBean imgObj = new CinemaImageBean();
				imgObj.setId(rs.getInt("id"));
				imgObj.setCinemaId(rs.getInt("cinema_id"));
				imgObj.setImgUrl(rs.getString("img_url"));
				imgObj.setCreatedDate(rs.getTimestamp("created_date"));
				imgList.add(imgObj);
			}

		} catch (SQLException e) {
			System.out.println("get imgs by cinema error : " + e.getMessage());
		}

		return imgList;
	}

	public List<FeatureBean> getFeatureByCinemaId(int cinemaId) {
		List<FeatureBean> featureList = new ArrayList<FeatureBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT f.* FROM feature f JOIN cinema_feature cf ON f.id = cf.feature_id WHERE cf.cinema_id = ? ORDER BY f.name;";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, cinemaId);

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
			System.out.println("get feature by cinema error : " + e.getMessage());
		}

		return featureList;
	}

	public int hardDeleteCinema(int cinemaId) {
		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "DELETE FROM `cinema` WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, cinemaId);
			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("delete cinema error : " + e.getMessage());
		}

		return i;
	}

}
