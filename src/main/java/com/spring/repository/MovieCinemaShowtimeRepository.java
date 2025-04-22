package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.model.CinemaBean;
import com.spring.model.MovieBean;
import com.spring.model.MovieCinemaShowtimeBean;
import com.spring.model.ShowtimeBean;

@Repository
public class MovieCinemaShowtimeRepository {

	@Autowired
	ShowtimeRepository showtimeRepo;

	@Autowired
	MovieRepository movieRepo;

	@Autowired
	CinemaRepository cinemaRepo;

	public MovieCinemaShowtimeBean getMovieCinemaShowtimeById(int mcsId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		MovieCinemaShowtimeBean movieShowtime = new MovieCinemaShowtimeBean();

		String sql = "SELECT mc.id, mc.movie_id, mc.cinema_id, mc.start_date, mc.end_date "
				+ "FROM movie_cinema mc WHERE mc.id = ?";

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, mcsId);
			rs = ps.executeQuery();

			if (rs.next()) {
				// Set MovieCinemaShowtimeBean id
				movieShowtime.setId(rs.getInt("id"));

				// Set Movie object using the repository method
				MovieBean movieObj = movieRepo.getMovieById(rs.getInt("movie_id"));
				movieShowtime.setMovieObj(movieObj);
				movieShowtime.setMovieId(rs.getInt("movie_id"));

				// Set Cinema object using the repository method
				CinemaBean cinemaObj = cinemaRepo.getCinemaById(rs.getInt("cinema_id"));
				movieShowtime.setCinemaObj(cinemaObj);
				movieShowtime.setCinemaId(rs.getInt("cinema_id"));

				// Set start and end dates
				movieShowtime.setLocalStartDate(rs.getDate("start_date").toLocalDate());
				movieShowtime.setLocalEndDate(rs.getDate("end_date").toLocalDate());

				// Fetch and set showtimes for the current movieShowtime
				movieShowtime.setShowtimeList(fetchShowtimesForMovieShowtime(movieShowtime.getId()));
			}
		} catch (SQLException e) {
			System.out.println("Error fetching movie cinema showtimes: " + e.getMessage());
		} finally {
			// Always close resources
			closeResources(rs, ps, con);
		}

		return movieShowtime;
	}

	public List<MovieCinemaShowtimeBean> getMovieCinemaShowtimeByCinemaId(int cinemaId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MovieCinemaShowtimeBean> movieShowtimeList = new ArrayList<>();

		String sql = "SELECT mc.id, mc.movie_id, mc.cinema_id, mc.start_date, mc.end_date "
				+ "FROM movie_cinema mc WHERE mc.cinema_id = ?";

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, cinemaId);
			rs = ps.executeQuery();

			while (rs.next()) {
				MovieCinemaShowtimeBean movieShowtime = new MovieCinemaShowtimeBean();
				movieShowtime.setId(rs.getInt("id"));

				// Set Movie object using repository method
				MovieBean movieObj = movieRepo.getMovieById(rs.getInt("movie_id"));
				movieShowtime.setMovieObj(movieObj);

				// Set Cinema object using repository method
				CinemaBean cinemaObj = cinemaRepo.getCinemaById(rs.getInt("cinema_id"));
				movieShowtime.setCinemaObj(cinemaObj);

				// Set start and end dates
				movieShowtime.setStartDate(rs.getDate("start_date"));
				movieShowtime.setEndDate(rs.getDate("end_date"));
				
				movieShowtime.setLocalStartDate(rs.getDate("start_date").toLocalDate());
				movieShowtime.setLocalEndDate(rs.getDate("end_date").toLocalDate());

				// Fetch and set showtimes for the current movieShowtime
				movieShowtime.setShowtimeList(fetchShowtimesForMovieShowtime(movieShowtime.getId()));

				movieShowtimeList.add(movieShowtime);
			}
		} catch (SQLException e) {
			System.out.println("Error fetching movie cinema showtimes: " + e.getMessage());
		} finally {
			closeResources(rs, ps, con);
		}

		return movieShowtimeList;
	}

	public List<MovieCinemaShowtimeBean> getFilteredMovieShowtimes(MovieCinemaShowtimeBean mcsObj, String selectedDate,
			Integer selectedShowtimeId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<MovieCinemaShowtimeBean> movieShowtimeList = new ArrayList<>();

		StringBuilder sql = new StringBuilder("SELECT mc.id, mc.movie_id, mc.cinema_id, mc.start_date, mc.end_date "
				+ "FROM movie_cinema mc JOIN cinema c ON mc.cinema_id = c.id "
				+ "JOIN movie m ON mc.movie_id = m.id WHERE 1=1 AND c.status = 1 AND m.status = 1");

		if (mcsObj.getCinemaId() > 0) {
			sql.append(" AND mc.cinema_id = ?");
		}
		if (mcsObj.getMovieId() > 0) {
			sql.append(" AND mc.movie_id = ?");
		}
		if (selectedDate != null && !selectedDate.isEmpty()) {
			sql.append(" AND ? BETWEEN mc.start_date AND mc.end_date");
		}
		if (selectedShowtimeId != null && selectedShowtimeId > 0) {
			sql.append(" AND mc.id IN (SELECT movie_cinema_id FROM moviecinema_showtime WHERE showtime_id = ?)");
		}

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql.toString());
			int paramIndex = 1;

			if (mcsObj.getCinemaId() > 0) {
				ps.setInt(paramIndex++, mcsObj.getCinemaId());
			}
			if (mcsObj.getMovieId() > 0) {
				ps.setInt(paramIndex++, mcsObj.getMovieId());
			}
			if (selectedDate != null && !selectedDate.isEmpty()) {
				ps.setString(paramIndex++, selectedDate);
			}
			if (selectedShowtimeId != null && selectedShowtimeId > 0) {
				ps.setInt(paramIndex++, selectedShowtimeId);
			}

			rs = ps.executeQuery();

			while (rs.next()) {
				MovieCinemaShowtimeBean movieShowtime = new MovieCinemaShowtimeBean();
				movieShowtime.setId(rs.getInt("id"));

				MovieBean movieObj = movieRepo.getMovieById(rs.getInt("movie_id"));
				movieShowtime.setMovieObj(movieObj);

				CinemaBean cinemaObj = cinemaRepo.getCinemaById(rs.getInt("cinema_id"));
				movieShowtime.setCinemaObj(cinemaObj);

				movieShowtime.setStartDate(rs.getDate("start_date"));
				movieShowtime.setEndDate(rs.getDate("end_date"));

				movieShowtime.setShowtimeList(fetchShowtimesForMovieShowtime(movieShowtime.getId()));

				movieShowtimeList.add(movieShowtime);
			}
		} catch (SQLException e) {
			System.out.println("Error fetching filtered movie showtimes: " + e.getMessage());
		} finally {
			closeResources(rs, ps, con);
		}

		return movieShowtimeList;
	}

	private List<ShowtimeBean> fetchShowtimesForMovieShowtime(int movieCinemaId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		List<ShowtimeBean> showtimeList = new ArrayList<>();
		String sql = "SELECT s.* FROM showtime s "
				+ "JOIN moviecinema_showtime mcs ON s.id = mcs.showtime_id WHERE mcs.movie_cinema_id = ?";

		try {
			con = DBConnection.getConnection();
			ps = con.prepareStatement(sql);
			ps.setInt(1, movieCinemaId);
			rs = ps.executeQuery();

			while (rs.next()) {
				ShowtimeBean showtime = new ShowtimeBean();
				showtime.setId(rs.getInt("id"));
				showtime.setStartHour(rs.getTime("start_hour"));
				showtime.setEndHour(rs.getTime("end_hour"));
				showtime.setStatus(rs.getInt("status"));
				showtimeList.add(showtime);
			}
		} catch (SQLException e) {
			System.out.println("Error fetching showtimes for movie cinema: " + e.getMessage());
		} finally {
			closeResources(rs, ps, con);
		}

		return showtimeList;
	}

	public int assignMovieCinemaShowtime(MovieCinemaShowtimeBean obj) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rss = null;

		int movieCinemaId = -1;

		String insertMovieCinemaSQL = "INSERT INTO `movie_cinema` (`movie_id`, `cinema_id`, `start_date`, `end_date`) "
				+ "VALUES (?, ?, ?, ?)";

		String insertShowtimeSQL = "INSERT INTO `moviecinema_showtime`(`movie_cinema_id`, `showtime_id`) VALUES (?, ?)";

		try {
			// Insert into movie_cinema table
			con = DBConnection.getConnection();
			ps = con.prepareStatement(insertMovieCinemaSQL, Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, obj.getMovieId());
			ps.setInt(2, obj.getCinemaId());
			ps.setDate(3, java.sql.Date.valueOf(obj.getLocalStartDate()));
			ps.setDate(4, java.sql.Date.valueOf(obj.getLocalEndDate()));

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						movieCinemaId = rs.getInt(1);
					}
				}
			} else {
				System.out.println("No movie-cinema assigned.");
				return -1;
			}

			// Insert into movie_cinema_showtime table for each selected showtime
			if (movieCinemaId > 0 && obj.getSelectedShowtimeIdList() != null) {
				PreparedStatement psShowtime = con.prepareStatement(insertShowtimeSQL);

				for (Integer showtimeId : obj.getSelectedShowtimeIdList()) {
					psShowtime.setInt(1, movieCinemaId);
					psShowtime.setInt(2, showtimeId);
					psShowtime.addBatch();
				}
				psShowtime.executeBatch(); // Execute batch insert
			}

			return movieCinemaId; // Return the inserted movie_cinema ID

		} catch (SQLException e) {
			System.out.println("Assign movie-cinema-showtime error: " + e.getMessage());
		} finally {
			// Always close resources
			closeResources(rss, ps, con);
		}

		return -1;
	}

	public boolean updateMovieCinemaShowtime(MovieCinemaShowtimeBean obj) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rss = null;

		boolean isUpdated = false;
		String updateMovieCinemaSQL = "UPDATE `movie_cinema` SET `movie_id` = ?, `cinema_id` = ?, `start_date` = ?, `end_date` = ? WHERE `id` = ?";
		String deleteShowtimeSQL = "DELETE FROM `moviecinema_showtime` WHERE `movie_cinema_id` = ?";
		String insertShowtimeSQL = "INSERT INTO `moviecinema_showtime`(`movie_cinema_id`, `showtime_id`) VALUES (?, ?)";

		try {
			// Start transaction
			con = DBConnection.getConnection();
			con.setAutoCommit(false);

			// Update movie_cinema table
			ps = con.prepareStatement(updateMovieCinemaSQL);
			ps.setInt(1, obj.getMovieId());
			ps.setInt(2, obj.getCinemaId());
			ps.setDate(3, java.sql.Date.valueOf(obj.getLocalStartDate()));
			ps.setDate(4, java.sql.Date.valueOf(obj.getLocalEndDate()));
			ps.setInt(5, obj.getId()); // movie_cinema ID

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected == 0) {
				System.out.println("No movie-cinema record updated.");
				con.rollback();
				return false;
			}

			// Delete existing showtimes for this movie_cinema
			PreparedStatement psDelete = con.prepareStatement(deleteShowtimeSQL);
			psDelete.setInt(1, obj.getId());
			psDelete.executeUpdate();

			// Insert new showtimes
			if (obj.getSelectedShowtimeIdList() != null && !obj.getSelectedShowtimeIdList().isEmpty()) {
				PreparedStatement psShowtime = con.prepareStatement(insertShowtimeSQL);
				for (Integer showtimeId : obj.getSelectedShowtimeIdList()) {
					psShowtime.setInt(1, obj.getId());
					psShowtime.setInt(2, showtimeId);
					psShowtime.addBatch();
				}
				psShowtime.executeBatch(); // Execute batch insert
			}

			// Commit transaction
			con.commit();
			isUpdated = true;
		} catch (SQLException e) {
			System.out.println("Update movie-cinema-showtime error: " + e.getMessage());
			try {
				con.rollback(); // Rollback transaction on error
			} catch (SQLException rollbackEx) {
				rollbackEx.printStackTrace();
			}
		} finally {
			try {
				con.setAutoCommit(true); // Reset auto-commit
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			closeResources(rss, ps, con);
		}

		return isUpdated;
	}

	public int hardDeleteMovieShowtime(int mcsId) {
		int i = 0;

		Connection con = DBConnection.getConnection();
		String sql = "DELETE FROM `movie_cinema` WHERE id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, mcsId);
			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("delete movie showtime error : " + e.getMessage());
		}

		return i;
	}

	private void closeResources(ResultSet rs, PreparedStatement ps, Connection con) {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
