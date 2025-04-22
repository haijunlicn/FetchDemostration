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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.model.CastBean;
import com.spring.model.CategoryBean;
import com.spring.model.CinemaBean;
import com.spring.model.CinemaImageBean;
import com.spring.model.MovieBean;
import com.spring.model.MovieCinemaShowtimeBean;
import com.spring.model.MovieImageBean;
import com.spring.model.RoleBean;
import com.spring.model.ShowtimeBean;

@Repository
public class MovieRepository {

	@Autowired
	MovieRepository movieRepo;

	private MovieBean mapResultSetToMovieBean(ResultSet rs) throws SQLException {
		MovieBean obj = new MovieBean();
		obj.setId(rs.getInt("id"));
		obj.setTitle(rs.getString("title"));
		obj.setDescription(rs.getString("description"));
		obj.setDuration(rs.getInt("duration"));
		obj.setPreviewUrl(rs.getString("preview_url"));
		obj.setRating(rs.getDouble("rating"));
		obj.setStatus(rs.getInt("status"));
		obj.setCreatedDate(rs.getTimestamp("created_date"));
		obj.setUpdatedDate(rs.getTimestamp("updated_date"));
		obj.setUploadedImgs(movieRepo.getImgsByMovieId(rs.getInt("id")));
		obj.setSelectedCastList(movieRepo.getCastByMovieId(rs.getInt("id")));
		obj.setSelectedCategoryList(movieRepo.getCategoryByMovieId(rs.getInt("id")));
		obj.setAiringDateList(movieRepo.getMovieCinemaByMovieId(rs.getInt("id")));
		return obj;
	}

	public List<MovieBean> getAllMovie() {
		List<MovieBean> movieList = new ArrayList<MovieBean>();
		String sql = "SELECT * FROM movie ORDER BY `created_date` DESC;";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				movieList.add(mapResultSetToMovieBean(rs));
			}

		} catch (SQLException e) {
			System.out.println("get movie error : " + e.getMessage());
		}

		return movieList;
	}

	public List<MovieBean> getActiveMovie() {
		List<MovieBean> movieList = new ArrayList<MovieBean>();
		String sql = "SELECT * FROM movie WHERE status = 1 ORDER BY created_date DESC;";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				movieList.add(mapResultSetToMovieBean(rs));
			}

		} catch (SQLException e) {
			System.out.println("get active movie error : " + e.getMessage());
		}

		return movieList;
	}
	
	public List<MovieBean> getCurrentShowingMovies() {
		List<MovieBean> movieList = new ArrayList<MovieBean>();
		String sql = "SELECT DISTINCT m.* FROM movie m "
					+ "JOIN movie_cinema mc ON m.id = mc.movie_id "
					+ "WHERE CURDATE() BETWEEN mc.start_date AND mc.end_date "
					+ "ORDER BY m.created_date DESC;";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				movieList.add(mapResultSetToMovieBean(rs));
			}

		} catch (SQLException e) {
			System.out.println("get active movie error : " + e.getMessage());
		}

		return movieList;
	}

	public List<MovieBean> getMoviesPaginated(int page, int size) {
		int offset = (page - 1) * size;
		List<MovieBean> movieList = new ArrayList<MovieBean>();
		String sql = "SELECT * FROM movie ORDER BY created_date DESC LIMIT ? OFFSET ?;";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, size);
			ps.setInt(2, offset);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					MovieBean obj = new MovieBean();
					obj.setId(rs.getInt("id"));
					obj.setTitle(rs.getString("title"));
					obj.setDescription(rs.getString("description"));
					obj.setDuration(rs.getInt("duration"));
					obj.setPreviewUrl(rs.getString("preview_url"));
					obj.setRating(rs.getDouble("rating"));
					obj.setStatus(rs.getInt("status"));
					obj.setCreatedDate(rs.getTimestamp("created_date"));
					obj.setUpdatedDate(rs.getTimestamp("updated_date"));
					movieList.add(obj);
				}
			}

		} catch (SQLException e) {
			System.out.println("get movie error : " + e.getMessage());
		}

		return movieList;
	}

	public int getTotalMovieCount() {
		int movieCount = -1;
		String sql = "SELECT COUNT(*) FROM movie";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				movieCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("get movie count : " + e.getMessage());
		}

		return movieCount;
	}

	public List<CategoryBean> getCategoryByMovieId(int movieId) {
		List<CategoryBean> cateList = new ArrayList<CategoryBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT c.* FROM category c JOIN movie_category mc ON c.id = mc.category_id WHERE mc.movie_id = ? ORDER BY c.name;";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, movieId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CategoryBean obj = new CategoryBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setStatus(rs.getInt("status"));
				cateList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get category by movie error : " + e.getMessage());
		}

		return cateList;
	}

	public List<CastBean> getCastByMovieId(int movieId) {
		List<CastBean> castList = new ArrayList<CastBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT c.*, c.id AS castId FROM cast c JOIN movie_cast mc ON c.id = mc.cast_id WHERE mc.movie_id = ? ORDER BY c.name;";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, movieId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CastBean obj = new CastBean();
				obj.setId(rs.getInt("castId"));
				obj.setName(rs.getString("name"));
				obj.setBirthdate(rs.getDate("birthdate"));
				obj.setNationality(rs.getString("nationality"));
				obj.setGender(rs.getString("gender"));
				obj.setStatus(rs.getInt("status"));
				castList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get cast by movie error : " + e.getMessage());
		}

		return castList;
	}

	public List<MovieCinemaShowtimeBean> getMovieCinemaByMovieId(int movieId) {
		List<MovieCinemaShowtimeBean> airingDateList = new ArrayList<MovieCinemaShowtimeBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT c.id AS cinema_id, c.name AS cinema_name, mc.* FROM cinema c JOIN movie_cinema mc ON c.id = mc.cinema_id WHERE mc.movie_id = ? ORDER BY c.name;";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, movieId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				MovieCinemaShowtimeBean obj = new MovieCinemaShowtimeBean();
				obj.setId(rs.getInt("id"));
				obj.setMovieId(rs.getInt("movie_id"));
				obj.setCinemaId(rs.getInt("cinema_id"));
				obj.getCinemaObj().setName(rs.getString("cinema_name"));
				obj.setStartDate(rs.getDate("start_date"));
				obj.setEndDate(rs.getDate("end_date"));
				airingDateList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get movieCinemaShowtime by movie error : " + e.getMessage());
		}

		return airingDateList;
	}

	public MovieBean getMovieById(int movieId) {
		MovieBean obj = new MovieBean();

		Connection con = DBConnection.getConnection();
		String sql = "SELECT * FROM movie WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, movieId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				obj.setId(rs.getInt("id"));
				obj.setTitle(rs.getString("title"));
				obj.setDescription(rs.getString("description"));
				obj.setDuration(rs.getInt("duration"));
				obj.setPreviewUrl(rs.getString("preview_url"));
				obj.setRating(rs.getDouble("rating"));
				obj.setStatus(rs.getInt("status"));
				obj.setCreatedDate(rs.getTimestamp("created_date"));
				obj.setUpdatedDate(rs.getTimestamp("updated_date"));
				obj.setUploadedImgs(getImgsByMovieId(movieId));
				obj.setSelectedCastList(getCastByMovieId(movieId));
				obj.setSelectedCategoryList(getCategoryByMovieId(movieId));
			}

		} catch (SQLException e) {
			System.out.println("get movie by id error : " + e.getMessage());
		}

		return obj;
	}

	public String getMovieTitleById(int movieId) {
		String movieTitle = "";

		Connection con = DBConnection.getConnection();
		String sql = "SELECT title FROM movie WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, movieId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				movieTitle = rs.getString("title");
			}

		} catch (SQLException e) {
			System.out.println("get movieTitle by id error : " + e.getMessage());
		}

		return movieTitle;
	}

	public List<MovieImageBean> getImgsByMovieId(int movieId) {
		List<MovieImageBean> imgList = new ArrayList<MovieImageBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT * FROM movie_image WHERE movie_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, movieId);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				MovieImageBean imgObj = new MovieImageBean();
				imgObj.setId(rs.getInt("id"));
				imgObj.setCinemaId(rs.getInt("movie_id"));
				imgObj.setImgUrl(rs.getString("img_url"));
				imgObj.setCreatedDate(rs.getTimestamp("created_date"));
				imgList.add(imgObj);
			}

		} catch (SQLException e) {
			System.out.println("get imgs by movie error : " + e.getMessage());
		}

		return imgList;
	}

	public List<CategoryBean> getAllCategory() {
		List<CategoryBean> cateList = new ArrayList<CategoryBean>();

		Connection con = DBConnection.getConnection();

		String sql = "SELECT * FROM `category`";

		try {
			PreparedStatement ps = con.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CategoryBean obj = new CategoryBean();
				obj.setId(rs.getInt("id"));
				obj.setName(rs.getString("name"));
				obj.setStatus(rs.getInt("status"));
				cateList.add(obj);
			}

		} catch (SQLException e) {
			System.out.println("get cateList error : " + e.getMessage());
		}

		return cateList;
	}

	public int addNewMovie(MovieBean movieObj) {
		int i = -1;

		Connection con = DBConnection.getConnection();

		String sql = "INSERT INTO `movie`(`title`, `description`, `duration`, "
				+ "`preview_url`, `status`) VALUES (?,?,?,?,?)";

		try {
			PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, movieObj.getTitle());
			ps.setString(2, movieObj.getDescription());
			ps.setInt(3, movieObj.getDuration());
			ps.setString(4, movieObj.getPreviewUrl());
			ps.setInt(5, movieObj.getStatus());
			i = ps.executeUpdate();

			if (i > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						int generatedId = rs.getInt(1);
						return generatedId;
					}
				}
			} else {
				System.out.println("No movie inserted.");
			}
		} catch (SQLException e) {
			System.out.println("add movie error : " + e.getMessage());
		}

		return i;
	}

	public int updateMovie(MovieBean movieObj) {
		int i = -1;
		Connection con = DBConnection.getConnection();

		// Corrected SQL query with proper column names for updating movie
		String sql = "UPDATE `movie` SET `title` = ?, `description` = ?, `duration` = ?, "
				+ "`preview_url` = ?, `status` = ? WHERE `id` = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, movieObj.getTitle());
			ps.setString(2, movieObj.getDescription());
			ps.setInt(3, movieObj.getDuration());
			ps.setString(4, movieObj.getPreviewUrl());
			ps.setInt(5, movieObj.getStatus());
			ps.setInt(6, movieObj.getId()); // Corrected to use movieObj

			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Update movie error: " + e.getMessage());
		}

		return i;
	}

	/*
	 * public void updateMovieCategories(int movieId, List<Integer> categoryIds) {
	 * Connection con = DBConnection.getConnection();
	 * 
	 * // SQL for inserting new rows that don't already exist String insertSql =
	 * "INSERT INTO movie_category (movie_id, category_id) " +
	 * "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM movie_category WHERE movie_id = ? AND category_id = ?)"
	 * ;
	 * 
	 * // SQL for deleting rows that are not in the provided list String deleteSql =
	 * "DELETE FROM movie_category WHERE movie_id = ? AND category_id NOT IN (?)";
	 * 
	 * try { // Convert the categoryIds list to a comma-separated string for the SQL
	 * query String cateIdsStr =
	 * categoryIds.stream().map(String::valueOf).collect(Collectors.joining(","));
	 * 
	 * // Delete rows that are not in the provided list PreparedStatement deletePs =
	 * con.prepareStatement(deleteSql); deletePs.setInt(1, movieId); // Set movieId
	 * deletePs.setString(2, cateIdsStr); // Set the comma-separated list of
	 * categoryIds deletePs.executeUpdate(); // Execute delete query
	 * 
	 * // Insert new rows that don't already exist PreparedStatement insertPs =
	 * con.prepareStatement(insertSql); for (int cateId : categoryIds) {
	 * insertPs.setInt(1, movieId); insertPs.setInt(2, cateId); insertPs.setInt(3,
	 * movieId); insertPs.setInt(4, cateId); insertPs.addBatch(); // Add to batch }
	 * insertPs.executeBatch(); // Execute batch insert
	 * 
	 * } catch (SQLException e) {
	 * System.out.println("Error updating movie categories: " + e.getMessage()); } }
	 * 
	 * public void updateMovieCasts(int movieId, List<Integer> castIds) { Connection
	 * con = DBConnection.getConnection();
	 * 
	 * // SQL for inserting new rows that don't already exist String insertSql =
	 * "INSERT INTO movie_cast (movie_id, cast_id) " +
	 * "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM movie_cast WHERE movie_id = ? AND cast_id = ?)"
	 * ;
	 * 
	 * // SQL for deleting rows that are not in the provided list String deleteSql =
	 * "DELETE FROM movie_cast WHERE movie_id = ? AND cast_id NOT IN (?)";
	 * 
	 * try { // Convert the categoryIds list to a comma-separated string for the SQL
	 * query String castIdsStr =
	 * castIds.stream().map(String::valueOf).collect(Collectors.joining(","));
	 * 
	 * // Delete rows that are not in the provided list PreparedStatement deletePs =
	 * con.prepareStatement(deleteSql); deletePs.setInt(1, movieId);
	 * deletePs.setString(2, castIdsStr); deletePs.executeUpdate();
	 * 
	 * // Insert new rows that don't already exist PreparedStatement insertPs =
	 * con.prepareStatement(insertSql); for (int castId : castIds) {
	 * insertPs.setInt(1, movieId); insertPs.setInt(2, castId); insertPs.setInt(3,
	 * movieId); insertPs.setInt(4, castId); insertPs.addBatch(); // Add to batch }
	 * insertPs.executeBatch(); // Execute batch insert
	 * 
	 * } catch (SQLException e) { System.out.println("Error updating movie casts: "
	 * + e.getMessage()); } }
	 */

	public void updateMovieCategories(int movieId, List<Integer> categoryIds) {
		Connection con = DBConnection.getConnection();

		// SQL for inserting new rows that don't already exist
		String insertSql = "INSERT INTO movie_category (movie_id, category_id) "
				+ "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM movie_category WHERE movie_id = ? AND category_id = ?)";

		try {
			if (!categoryIds.isEmpty()) {
				StringBuilder deleteSql = new StringBuilder(
						"DELETE FROM movie_category WHERE movie_id = ? AND category_id NOT IN (");
				deleteSql.append("?,".repeat(categoryIds.size()));
				deleteSql.setLength(deleteSql.length() - 1);
				deleteSql.append(")");

				PreparedStatement deletePs = con.prepareStatement(deleteSql.toString());
				deletePs.setInt(1, movieId);
				for (int i = 0; i < categoryIds.size(); i++) {
					deletePs.setInt(i + 2, categoryIds.get(i));
				}
				deletePs.executeUpdate();
				deletePs.close();
			}

			// Insert new rows that don't already exist
			PreparedStatement insertPs = con.prepareStatement(insertSql);
			for (int cateId : categoryIds) {
				insertPs.setInt(1, movieId);
				insertPs.setInt(2, cateId);
				insertPs.setInt(3, movieId);
				insertPs.setInt(4, cateId);
				insertPs.addBatch();
			}
			insertPs.executeBatch();
			insertPs.close();

		} catch (SQLException e) {
			System.out.println("Error updating movie categories: " + e.getMessage());
		}
	}

	public void updateMovieCasts(int movieId, List<Integer> castIds) {
		Connection con = DBConnection.getConnection();

		// SQL for inserting new rows that don't already exist
		String insertSql = "INSERT INTO movie_cast (movie_id, cast_id) "
				+ "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM movie_cast WHERE movie_id = ? AND cast_id = ?)";

		try {
			if (!castIds.isEmpty()) {
				StringBuilder deleteSql = new StringBuilder(
						"DELETE FROM movie_cast WHERE movie_id = ? AND cast_id NOT IN (");
				deleteSql.append("?,".repeat(castIds.size()));
				deleteSql.setLength(deleteSql.length() - 1);
				deleteSql.append(")");

				PreparedStatement deletePs = con.prepareStatement(deleteSql.toString());
				deletePs.setInt(1, movieId);
				for (int i = 0; i < castIds.size(); i++) {
					deletePs.setInt(i + 2, castIds.get(i));
				}
				deletePs.executeUpdate();
				deletePs.close();
			}

			// Insert new rows that don't already exist
			PreparedStatement insertPs = con.prepareStatement(insertSql);
			for (int castId : castIds) {
				insertPs.setInt(1, movieId);
				insertPs.setInt(2, castId);
				insertPs.setInt(3, movieId);
				insertPs.setInt(4, castId);
				insertPs.addBatch();
			}
			insertPs.executeBatch();
			insertPs.close();

		} catch (SQLException e) {
			System.out.println("Error updating movie casts: " + e.getMessage());
		}
	}

	public void updateMovieImages(int movieId, List<String> imgUrlList, List<Integer> toDeleteImgIdList) {
		Connection con = DBConnection.getConnection();

		// SQL for inserting new images
		String insertSql = "INSERT INTO movie_image (movie_id, img_url) VALUES (?, ?)";

		try {
			if (!toDeleteImgIdList.isEmpty()) {
				StringBuilder deleteSql = new StringBuilder("DELETE FROM movie_image WHERE id IN (");
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
					insertPs.setInt(1, movieId);
					insertPs.setString(2, imgUrl);
					insertPs.addBatch();
				}
				insertPs.executeBatch();
				insertPs.close();
			}

		} catch (SQLException e) {
			System.out.println("Error updating movie images: " + e.getMessage());
		}
	}

	public int hardDeleteMovie(int movieId) {
		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "DELETE FROM `movie` WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, movieId);
			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("delete movie error : " + e.getMessage());
		}
		return i;
	}

}
