package com.spring.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.spring.model.AccountBean;
import com.spring.model.BookingBean;
import com.spring.model.CinemaBean;
import com.spring.model.McsBean;
import com.spring.model.MovieBean;
import com.spring.model.MovieCinemaShowtimeBean;
import com.spring.model.PaymentBean;
import com.spring.model.SeatBean;
import com.spring.model.ShowtimeBean;
import com.spring.repository.BookingRepository;
import com.spring.repository.CinemaRepository;
import com.spring.repository.MovieCinemaShowtimeRepository;
import com.spring.repository.MovieRepository;
import com.spring.repository.PaymentRepository;
import com.spring.repository.SeatRepository;
import com.spring.repository.ShowtimeRepository;
import com.spring.repository.SupabaseRepository;

@Controller
@MultipartConfig
public class IndexController {

	@Autowired
	private CinemaRepository cinemaRepo;

	@Autowired
	private MovieRepository movieRepo;

	@Autowired
	private MovieCinemaShowtimeRepository mcsRepo;

	@Autowired
	private ShowtimeRepository showtimeRepo;

	@Autowired
	private BookingRepository bookingRepo;

	@Autowired
	private SeatRepository seatRepo;

	@Autowired
	private PaymentRepository paymentRepo;

	@Autowired
	private SupabaseRepository supaRepo;

	@GetMapping("/")
	public ModelAndView showIndex(Model model) {
		return prepareFilterForm("customer/index", model);
	}

	@GetMapping("/showFilteredMovieShowtimes")
	public ModelAndView showFilteredMovies(@ModelAttribute("mcsObj") MovieCinemaShowtimeBean mcsObj,
			@RequestParam(value = "selectedDate", required = false) String selectedDate,
			@RequestParam(value = "selectedShowtimeId", required = false) String selectedShowtimeId,
			@RequestParam(value = "cinemaId", required = false) String cinemaId,
			@RequestParam(value = "movieId", required = false) String movieId, Model model) {

		mcsObj.setCinemaId(parseInteger(cinemaId, 0));
		mcsObj.setMovieId(parseInteger(movieId, 0));

		// If selectedDate is null or empty, set it to tomorrow's date
		if (selectedDate == null || selectedDate.trim().isEmpty()) {
			selectedDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}

		Integer showtimeId = null;
		if (selectedShowtimeId != null && !selectedShowtimeId.isEmpty()) {
			showtimeId = Integer.parseInt(selectedShowtimeId);
		}

		List<MovieCinemaShowtimeBean> mcsObjList = mcsRepo.getFilteredMovieShowtimes(mcsObj,
				convertDateFormat(selectedDate, "dd/MM/yyyy", "yyyy-MM-dd"), showtimeId);

		model.addAttribute("mcsObjList", mcsObjList);
		model.addAttribute("mcsObj", mcsObj);
		model.addAttribute("selectedDate", selectedDate);
		model.addAttribute("selectedShowtimeId", showtimeId);
		return prepareFilterForm("customer/moviesTest", model);
	}

	@GetMapping("/booking/bookSeats/{movieCinemaShowtimeId}")
	public ModelAndView showBookSeatsPage(@ModelAttribute("bookingObj") BookingBean bookingObj, Model model,
			HttpSession session, @PathVariable("movieCinemaShowtimeId") int mcsId,
			@RequestParam(value = "selectedDate", required = false) String selectedDate,
			@RequestParam(value = "selectedShowtimeId", required = false) String selectedShowtimeId) {

		// If selectedDate is null or empty, set it to tomorrow's date
		if (selectedDate == null || selectedDate.trim().isEmpty()) {
			selectedDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}

		MovieCinemaShowtimeBean mcsObj = mcsRepo.getMovieCinemaShowtimeById(mcsId);
		model.addAttribute("mcsObj", mcsObj);
		model.addAttribute("selectedDate", selectedDate);
		model.addAttribute("selectedShowtimeId", selectedShowtimeId);

		// Check if sessionBookingObj already exists in the session
		BookingBean sessionBookingObj = (BookingBean) session.getAttribute("sessionBookingObj");
		if (sessionBookingObj == null) {
			session.setAttribute("sessionBookingObj", bookingObj);
		}

		model.addAttribute("sessionBookingObj", sessionBookingObj != null ? sessionBookingObj : bookingObj);

		return new ModelAndView("customer/bookSeats");
	}

	@PostMapping("/updateBookingSeat")
	public ResponseEntity<Map<String, Object>> updateBookingSeat(@RequestBody Map<String, Object> requestData) {

		Map<String, Object> response = new HashMap<>();

		try {
			System.out.println("updateBookingSeat API called with data: " + requestData);

			if (!requestData.containsKey("showtimeId") || !requestData.containsKey("bookingDate")
					|| !requestData.containsKey("mcId")) {
				System.out.println("Invalid request data: Missing showtimeId, bookingDate, or mcId.");
				response.put("success", false);
				response.put("message", "Missing required fields for showtime, booking date, or mcId.");
				return ResponseEntity.badRequest().body(response);
			}

			// String newShowtimeId = (String) requestData.get("showtimeId");
			Integer newMcId = Integer.parseInt((String) requestData.get("mcId"));
			Integer newShowtimeIdInt = Integer.parseInt((String) requestData.get("showtimeId"));
			String newBookingDate = (String) requestData.get("bookingDate");
			Date formattedDate = convertStringDateToDateObject(newBookingDate, "dd/MM/yyyy");
			
			McsBean MCSobj = bookingRepo.getMcsObjByMcAndShowtimeId(newMcId, newShowtimeIdInt);
			List<Integer> bookingIds = bookingRepo.getBookingIdsByMcsIdAndDate(MCSobj.getId(), formattedDate);
			List<Integer> occupiedSeatIdList = bookingRepo.getSeatIdsByBookingIds(bookingIds);
			
			System.out.println("MCSobj : " + MCSobj);
			System.out.println("booking ids : " + bookingIds);
			System.out.println("occupied seat ids : " + occupiedSeatIdList);

			response.put("success", true);
			response.put("occupiedSeatIdList", occupiedSeatIdList);
			response.put("message", "Updated booked seat list successfully!");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			System.err.println("Error occurred while processing seat selection: " + e.getMessage());
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "Error processing seat selection: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	private ModelAndView prepareFilterForm(String viewName, Model model) {
		List<CinemaBean> cinemaList = cinemaRepo.getActiveCinema();
		List<MovieBean> movieList = movieRepo.getCurrentShowingMovies();
		List<ShowtimeBean> showtimeList = showtimeRepo.getActiveShowtimes();

		model.addAttribute("cinemaList", cinemaList);
		model.addAttribute("movieList", movieList);
		model.addAttribute("showtimeList", showtimeList);

		// Preserve existing mcsObj if available
		if (!model.containsAttribute("mcsObj")) {
			model.addAttribute("mcsObj", new MovieCinemaShowtimeBean());
		}

		return new ModelAndView(viewName);
	}

	private int parseInteger(String value, int defaultValue) {
		try {
			return (value != null && !value.isEmpty()) ? Integer.parseInt(value) : defaultValue;
		} catch (NumberFormatException e) {
			System.err.println("Error parsing integer: " + e.getMessage());
			return defaultValue;
		}
	}

	private String convertDateFormat(String date, String inputFormat, String outputFormat) {
		try {
			SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
			SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);
			Date parsedDate = inputFormatter.parse(date);
			return outputFormatter.format(parsedDate);
		} catch (ParseException e) {
			System.err.println("Error parsing date: " + e.getMessage());
			return null;
		}
	}

	private Date convertStringDateToDateObject(String date, String inputFormat) {
		try {
			SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
			return inputFormatter.parse(date); // Returns the Date object
		} catch (ParseException e) {
			System.err.println("Error parsing date: " + e.getMessage());
			return null;
		}
	}

	public static Date convertStringToDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}