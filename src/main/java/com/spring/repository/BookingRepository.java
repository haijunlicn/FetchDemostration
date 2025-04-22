package com.spring.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.model.AccountBean;
import com.spring.model.BookingBean;
import com.spring.model.CinemaBean;
import com.spring.model.McsBean;
import com.spring.model.MovieBean;

@Repository
public class BookingRepository {

	@Autowired
	SeatRepository seatRepo;

	@Autowired
	MovieRepository movieRepo;

	@Autowired
	CinemaRepository cinemaRepo;

	@Autowired
	ShowtimeRepository showtimeRepo;

	public McsBean getMcsObjByMcAndShowtimeId(int mcId, int showtimeId) {
		McsBean mcsObj = null; // Default value if not found

		Connection con = DBConnection.getConnection();
		String sql = "SELECT * FROM moviecinema_showtime WHERE movie_cinema_id = ? AND showtime_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, mcId);
			ps.setInt(2, showtimeId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				mcsObj = new McsBean();
				mcsObj.setId(rs.getInt("id"));
				mcsObj.setMcId(rs.getInt("movie_cinema_id"));
				mcsObj.setShowtimeId(rs.getInt("showtime_id"));
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving movie cinema showtime object: " + e.getMessage());
		}

		return mcsObj;
	}

	public List<Integer> getBookingIdsByMcsIdAndDate(int mcsId, Date bookingDate) {
		List<Integer> bookingIds = new ArrayList<Integer>();
		Connection con = DBConnection.getConnection();

		String sql = "SELECT id FROM booking WHERE mcs_id = ? AND booking_date = ? AND status In (?,?)";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, mcsId);
			ps.setDate(2, new java.sql.Date(bookingDate.getTime()));
			ps.setInt(3, 1); // status 1 - pending
			ps.setInt(4, 2); // status 2 - confirmed

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					bookingIds.add(rs.getInt("id"));
				}
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving booking IDs: " + e.getMessage());
		}

		return bookingIds;
	}

	public Integer getMcsIdByMcAndShowtimeId(int mcId, int showtimeId) {
		Integer mcsId = null;

		String sql = "SELECT id FROM moviecinema_showtime WHERE movie_cinema_id = ? AND showtime_id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setInt(1, mcId);
			ps.setInt(2, showtimeId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					mcsId = rs.getInt("id");
				}
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving movie cinema showtime ID: " + e.getMessage());
		}

		return mcsId;
	}

	public int addBooking(BookingBean bookingObj) {
		int generatedId = -1;

		String sql = "INSERT INTO `booking` (`customer_id`, `mcs_id`, `total_amount`, `booking_date`, "
				+ "`status`, `payment_method_id`, `screenshot_path`) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, bookingObj.getCusId());
			ps.setInt(2, bookingObj.getMcsId());
			ps.setDouble(3, bookingObj.getTotalAmount());
			ps.setDate(4, new java.sql.Date(bookingObj.getBookingDate().getTime()));
			ps.setInt(5, 1); // status is 1-pending by default
			ps.setInt(6, bookingObj.getPaymentMethodId());
			ps.setString(7, bookingObj.getScreenshotPath());

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						generatedId = rs.getInt(1);
					}
				}
			} else {
				System.out.println("No booking inserted.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return generatedId;
	}

	public void addBookingSeats(int bookingId, List<Integer> seatIds) {
		String insertSql = "INSERT INTO booking_seat (booking_id, seat_id) "
				+ "SELECT ?, ? WHERE NOT EXISTS (SELECT 1 FROM booking_seat WHERE booking_id = ? AND seat_id = ?)";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement insertPs = con.prepareStatement(insertSql)) {

			for (int seatId : seatIds) {
				insertPs.setInt(1, bookingId);
				insertPs.setInt(2, seatId);
				insertPs.setInt(3, bookingId);
				insertPs.setInt(4, seatId);
				insertPs.addBatch(); // Add to batch for efficiency
			}

			insertPs.executeBatch(); // Execute batch insert

		} catch (SQLException e) {
			System.out.println("Error adding booking seats: " + e.getMessage());
		}
	}

	public List<Integer> getSeatIdsByBookingIds(List<Integer> bookingIds) {
		List<Integer> seatIds = new ArrayList<>();
		Connection con = DBConnection.getConnection();

		if (bookingIds == null || bookingIds.isEmpty()) {
			return seatIds; // Return empty list if no booking IDs are provided
		}

		// Dynamically construct SQL query with the correct number of placeholders
		String placeholders = bookingIds.stream().map(id -> "?").collect(Collectors.joining(", "));
		String sql = "SELECT seat_id FROM booking_seat WHERE booking_id IN (" + placeholders + ")";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			// Set booking IDs dynamically
			for (int i = 0; i < bookingIds.size(); i++) {
				ps.setInt(i + 1, bookingIds.get(i));
			}

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					seatIds.add(rs.getInt("seat_id"));
				}
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving seat IDs: " + e.getMessage());
		}

		return seatIds;
	}

	public List<BookingBean> getBookingListByCusId(int cusId) {
		String query = "SELECT b.id, b.customer_id, b.mcs_id, b.total_amount, b.booking_date, b.screenshot_path, b.status, "
				+ "b.promo_id, b.payment_method_id, b.admin_id, b.created_date, pm.method_name, mcs.showtime_id, mcs.movie_cinema_id, "
				+ "mc.movie_id, mc.cinema_id FROM booking b " + "JOIN moviecinema_showtime mcs ON b.mcs_id = mcs.id "
				+ "JOIN movie_cinema mc ON mcs.movie_cinema_id = mc.id "
				+ "JOIN payment_method pm ON b.payment_method_id = pm.id "
				+ "WHERE b.customer_id = ? ORDER BY b.created_date DESC";

		List<BookingBean> bookingList = new ArrayList<>();

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, cusId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				BookingBean booking = new BookingBean();
				booking.setId(rs.getInt("b.id"));
				booking.setCusId(rs.getInt("b.customer_id"));
				booking.setMcsId(rs.getInt("b.mcs_id"));
				booking.setTotalAmount(rs.getDouble("b.total_amount"));
				booking.setBookingDate(rs.getDate("b.booking_date"));
				List<Integer> bookingIds = new ArrayList<Integer>();
				bookingIds.add(booking.getId());
				booking.setBookedSeatIdList(getSeatIdsByBookingIds(bookingIds));
				booking.setBookedSeatList(seatRepo.getSeatsByIds(booking.getBookedSeatIdList()));
				booking.setScreenshotPath(rs.getString("b.screenshot_path"));
				booking.setStatus(rs.getInt("b.status"));
				booking.setPaymentMethodId(rs.getInt("b.payment_method_id"));
				booking.setPaymentMethodName(rs.getString("pm.method_name"));
				booking.setAdminId(rs.getInt("b.admin_id"));
				booking.setCreatedDate(rs.getTimestamp("b.created_date"));
				booking.setSelectedShowtimeObj(showtimeRepo.getShowtimeById(rs.getInt("mcs.showtime_id")));
				booking.setMovieId(rs.getInt("mc.movie_id"));
				booking.setCinemaId(rs.getInt("mc.cinema_id"));
				booking.setMovieTitle(movieRepo.getMovieTitleById(rs.getInt("mc.movie_id")));
				booking.setCinemaName(cinemaRepo.getCinemaNameById(rs.getInt("mc.cinema_id")));
				bookingList.add(booking);
			}

		} catch (SQLException e) {
			System.out.println("Error fetching bookings: " + e.getMessage());
		}
		return bookingList;
	}

	public BookingBean getBookingById(int bookingId) {

		String query = "SELECT b.id, b.customer_id, b.mcs_id, b.total_amount, b.booking_date, b.screenshot_path, b.status, "
				+ "b.promo_id, b.payment_method_id, b.admin_id, b.created_date, pm.method_name, mcs.showtime_id, mcs.movie_cinema_id, "
				+ "mc.movie_id, mc.cinema_id, a.name, a.email FROM booking b "
				+ "JOIN moviecinema_showtime mcs ON b.mcs_id = mcs.id "
				+ "JOIN movie_cinema mc ON mcs.movie_cinema_id = mc.id "
				+ "JOIN payment_method pm ON b.payment_method_id = pm.id " + "JOIN account a ON b.customer_id = a.id "
				+ "WHERE b.id = ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ps.setInt(1, bookingId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				BookingBean booking = new BookingBean();
				booking.setId(rs.getInt("b.id"));
				booking.setCusId(rs.getInt("b.customer_id"));
				booking.setMcsId(rs.getInt("b.mcs_id"));
				booking.setTotalAmount(rs.getDouble("b.total_amount"));
				booking.setBookingDate(rs.getDate("b.booking_date"));
				List<Integer> bookingIds = new ArrayList<>();
				bookingIds.add(booking.getId());
				booking.setBookedSeatIdList(getSeatIdsByBookingIds(bookingIds));
				booking.setBookedSeatList(seatRepo.getSeatsByIds(booking.getBookedSeatIdList()));
				booking.setScreenshotPath(rs.getString("b.screenshot_path"));
				booking.setStatus(rs.getInt("b.status"));
				booking.setPaymentMethodId(rs.getInt("b.payment_method_id"));
				booking.setPaymentMethodName(rs.getString("pm.method_name"));
				booking.setAdminId(rs.getInt("b.admin_id"));
				booking.setCreatedDate(rs.getTimestamp("b.created_date"));
				booking.setSelectedShowtimeObj(showtimeRepo.getShowtimeById(rs.getInt("mcs.showtime_id")));
				booking.setMovieId(rs.getInt("mc.movie_id"));
				booking.setCinemaId(rs.getInt("mc.cinema_id"));
				booking.setMovieTitle(movieRepo.getMovieTitleById(rs.getInt("mc.movie_id")));
				booking.setCinemaName(cinemaRepo.getCinemaNameById(rs.getInt("mc.cinema_id")));
				booking.setCusName(rs.getString("a.name"));
				System.out.println(booking);
				return booking;
			}
		} catch (SQLException e) {
			System.out.println("Error fetching booking by ID: " + e.getMessage());
		}
		return null;
	}

	public int updateBookingStatus(int bookingId, int adminId, int status) {
		int i = -1;
		Connection con = DBConnection.getConnection();
		String sql = "UPDATE `booking` SET `status` = ?, `admin_id` = ? WHERE `id` = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, status);
			ps.setInt(2, adminId);
			ps.setInt(3, bookingId);
			i = ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Update booking status error: " + e.getMessage());
		}

		return i;
	}

	public List<BookingBean> getBookingsPaginated(int page, int size, int cinemaId) {
		List<BookingBean> bookingList = new ArrayList<>();
		int offset = (page - 1) * size;

		// Start building the query with the base SELECT and JOINs
		String query = "SELECT b.id, b.customer_id, b.mcs_id, b.total_amount, b.booking_date, b.screenshot_path, b.status, "
				+ "b.promo_id, b.payment_method_id, b.admin_id, b.created_date, pm.method_name, mcs.showtime_id, mcs.movie_cinema_id, "
				+ "mc.movie_id, mc.cinema_id, a.name, a.email FROM booking b "
				+ "JOIN moviecinema_showtime mcs ON b.mcs_id = mcs.id "
				+ "JOIN movie_cinema mc ON mcs.movie_cinema_id = mc.id "
				+ "JOIN payment_method pm ON b.payment_method_id = pm.id " + "JOIN account a ON b.customer_id = a.id ";

		// Add WHERE clause based on cinemaId if it's not 0
		if (cinemaId != 0) {
			query += "WHERE mc.cinema_id = ? ";
		}

		// Add ordering and pagination
		query += "ORDER BY CASE WHEN b.status = 1 THEN 0 ELSE 1 END, b.created_date DESC " + "LIMIT ? OFFSET ?";

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {

			// Set parameters for the query
			int paramIndex = 1;
			if (cinemaId != 0) {
				ps.setInt(paramIndex++, cinemaId); // Set cinemaId if not 0
			}
			ps.setInt(paramIndex++, size); // Set size (LIMIT)
			ps.setInt(paramIndex++, offset); // Set offset

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					BookingBean booking = new BookingBean();
					booking.setId(rs.getInt("b.id"));
					booking.setCusId(rs.getInt("b.customer_id"));
					booking.setMcsId(rs.getInt("b.mcs_id"));
					booking.setTotalAmount(rs.getDouble("b.total_amount"));
					booking.setBookingDate(rs.getDate("b.booking_date"));
					List<Integer> bookingIds = new ArrayList<>();
					bookingIds.add(booking.getId());
					booking.setBookedSeatIdList(getSeatIdsByBookingIds(bookingIds));
					booking.setBookedSeatList(seatRepo.getSeatsByIds(booking.getBookedSeatIdList()));
					booking.setScreenshotPath(rs.getString("b.screenshot_path"));
					booking.setStatus(rs.getInt("b.status"));
					booking.setPaymentMethodId(rs.getInt("b.payment_method_id"));
					booking.setPaymentMethodName(rs.getString("pm.method_name"));
					booking.setAdminId(rs.getInt("b.admin_id"));
					booking.setCreatedDate(rs.getTimestamp("b.created_date"));
					booking.setSelectedShowtimeObj(showtimeRepo.getShowtimeById(rs.getInt("mcs.showtime_id")));
					booking.setMovieId(rs.getInt("mc.movie_id"));
					booking.setCinemaId(rs.getInt("mc.cinema_id"));
					booking.setMovieTitle(movieRepo.getMovieTitleById(rs.getInt("mc.movie_id")));
					booking.setCinemaName(cinemaRepo.getCinemaNameById(rs.getInt("mc.cinema_id")));
					booking.setCusName(rs.getString("a.name"));
					bookingList.add(booking);
				}
			}

		} catch (SQLException e) {
			System.out.println("get booking error : " + e.getMessage());
		}

		return bookingList;
	}

	public List<BookingBean> getAllBookings() {
		String query = "SELECT b.id, b.customer_id, b.mcs_id, b.total_amount, b.booking_date, b.screenshot_path, b.status, "
				+ "b.promo_id, b.payment_method_id, b.admin_id, b.created_date, pm.method_name, mcs.showtime_id, mcs.movie_cinema_id, "
				+ "mc.movie_id, mc.cinema_id, a.name, a.email FROM booking b "
				+ "JOIN moviecinema_showtime mcs ON b.mcs_id = mcs.id "
				+ "JOIN movie_cinema mc ON mcs.movie_cinema_id = mc.id "
				+ "JOIN payment_method pm ON b.payment_method_id = pm.id " + "JOIN account a ON b.customer_id = a.id "
				+ "ORDER BY b.created_date DESC";

		List<BookingBean> bookingList = new ArrayList<>();

		try (Connection con = DBConnection.getConnection(); PreparedStatement ps = con.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				BookingBean booking = new BookingBean();
				booking.setId(rs.getInt("b.id"));
				booking.setCusId(rs.getInt("b.customer_id"));
				booking.setMcsId(rs.getInt("b.mcs_id"));
				booking.setTotalAmount(rs.getDouble("b.total_amount"));
				booking.setBookingDate(rs.getDate("b.booking_date"));
				List<Integer> bookingIds = new ArrayList<>();
				bookingIds.add(booking.getId());
				booking.setBookedSeatIdList(getSeatIdsByBookingIds(bookingIds));
				booking.setBookedSeatList(seatRepo.getSeatsByIds(booking.getBookedSeatIdList()));
				booking.setScreenshotPath(rs.getString("b.screenshot_path"));
				booking.setStatus(rs.getInt("b.status"));
				booking.setPaymentMethodId(rs.getInt("b.payment_method_id"));
				booking.setPaymentMethodName(rs.getString("pm.method_name"));
				booking.setAdminId(rs.getInt("b.admin_id"));
				booking.setCreatedDate(rs.getTimestamp("b.created_date"));
				booking.setSelectedShowtimeObj(showtimeRepo.getShowtimeById(rs.getInt("mcs.showtime_id")));
				booking.setMovieId(rs.getInt("mc.movie_id"));
				booking.setCinemaId(rs.getInt("mc.cinema_id"));
				booking.setMovieTitle(movieRepo.getMovieTitleById(rs.getInt("mc.movie_id")));
				booking.setCinemaName(cinemaRepo.getCinemaNameById(rs.getInt("mc.cinema_id")));
				booking.setCusName(rs.getString("a.name"));
				bookingList.add(booking);
			}

		} catch (SQLException e) {
			System.out.println("Error fetching bookings: " + e.getMessage());
		}

		return bookingList;
	}

	public int getTotalBookingCount() {
		int cinemaCount = -1;
		String sql = "SELECT COUNT(*) FROM booking";

		try (Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				cinemaCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("get booking count : " + e.getMessage());
		}

		return cinemaCount;
	}

}
