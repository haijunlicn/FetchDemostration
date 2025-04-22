<%-- <%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Movie Theme</title>
<style>
.carousel-caption h3 {
	font-size: 1.5rem;
}

.carousel-caption h1 {
	font-size: 2rem;
}

.carousel-caption p {
	font-size: 1rem;
	width: 50%;
	margin: auto;
}

.button_1 {
	font-size: 0.9rem;
	padding: 10px 15px;
}

@
keyframes zoomIn {from { transform:scale(0.5);
	opacity: 0;
}

to {
	transform: scale(1);
	opacity: 1;
}

}
h1 {
	animation: zoomIn 0.5s ease-in-out;
}
</style>

</head>
<body>
	<div class="main clearfix position-relative">

		<jsp:include page="../cusHeader.jsp"></jsp:include>

		<div class="main_2 clearfix">
			<section id="center" class="center_home">
				<div id="carouselExampleCaptions" class="carousel slide"
					data-bs-ride="carousel">
					<div class="carousel-indicators">
						<button type="button" data-bs-target="#carouselExampleCaptions"
							data-bs-slide-to="0" class="active" aria-label="Slide 1"></button>
						<button type="button" data-bs-target="#carouselExampleCaptions"
							data-bs-slide-to="1" aria-label="Slide 2"></button>
						<button type="button" data-bs-target="#carouselExampleCaptions"
							data-bs-slide-to="2" aria-label="Slide 3"></button>
					</div>
					<div class="carousel-inner">
						<div class="carousel-item active">
							<img src="resources/cusTemplate/img/dragon1.jpg" class="d-block w-100"
								alt="...">
							<div class="carousel-caption d-md-block">
								<h3 class="col_oran">Adventurer Movie</h3>
								<h1 class="text-white mt-2">How to Train Your Dragon Live
									Action</h1>
								<p class="mt-2 text-light">The story centers around a Viking
									teenager, who lives on the Isle of Berk, where fighting dragons
									is a way of life.</p>
								<ul class="mb-0 mt-3">
									<li class="d-inline-block ms-2"><a class="button_1"
										href="#"><i class="fa fa-check-circle me-1"></i> Get
											Ticket</a></li>
								</ul>
							</div>
						</div>
						<div class="carousel-item">
							<img src="resources/cusTemplate/img/DunePoster.jpg" class="d-block w-100"
								alt="...">
							<div class="carousel-caption d-md-block">
								<h3 class="col_oran">Action and Thriller</h3>
								<h1 class="text-white mt-2">Dune: Part Two</h1>
								<p class="mt-2 text-light">Paul Atreides unites with Chani
									and the Fremen while seeking revenge against the conspirators
									who destroyed his family.</p>
								<ul class="mb-0 mt-3">
									<li class="d-inline-block ms-2"><a class="button_1"
										href="#"><i class="fa fa-check-circle me-1"></i> Get
											Ticket</a></li>
								</ul>
							</div>
						</div>
						<div class="carousel-item">
							<img src="resources/cusTemplate/img/DeadPool1.jpg" class="d-block w-100"
								alt="...">
							<div class="carousel-caption d-md-block">
								<h3 class="col_oran">Action and Comedy</h3>
								<h1 class="text-white mt-2">Deadpool & Wolverine</h1>
								<p class="mt-2 text-light">Deadpool teams up with a
									reluctant Wolverine from another universe to stop the TVA from
									destroying his own universe.</p>
								<ul class="mb-0 mt-3">
									<li class="d-inline-block ms-2"><a class="button_1"
										href="#"><i class="fa fa-check-circle me-1"></i> Get
											Ticket</a></li>
								</ul>
							</div>
						</div>
					</div>
					<button class="carousel-control-prev" type="button"
						data-bs-target="#carouselExampleCaptions" data-bs-slide="prev">
						<span class="carousel-control-prev-icon" aria-hidden="true"></span>
						<span class="visually-hidden">Previous</span>
					</button>
					<button class="carousel-control-next" type="button"
						data-bs-target="#carouselExampleCaptions" data-bs-slide="next">
						<span class="carousel-control-next-icon" aria-hidden="true"></span>
						<span class="visually-hidden">Next</span>
					</button>
				</div>
			</section>
		</div>

	</div>

	<div class="border_dashed"></div>

	<section id="feat" class="p_3 bg-light">
		<div class="container-xl">
			<div class="row feat_1 mb-4">
				<div class="col-md-6">
					<div class="feat_1l">
						<span class="fa fa-film col_oran"></span>
						<h6 class="text-muted mt-3">Checkout New Movies</h6>
						<h1 class="mb-0 font_50">Movies coming soon!</h1>
					</div>
				</div>
				<div class="col-md-6">
					<div class="feat_1r mt-5">
						<p class="mb-0">You can watch movie trailer here!</p>
					</div>
				</div>
			</div>

			<div class="row feat_2">
				<!-- Movie 1 -->
				<div class="col-md-4">
					<div class="feat_2i position-relative">
						<div class="feat_2i1">
							<div class="grid clearfix">
								<figure class="effect-jazz mb-0">
									<a href="#"><img src="resources/cusTemplate/img/dragon.jpg"
										class="w-100" alt="abc"></a>
								</figure>
							</div>
						</div>
						<div class="feat_2i2 position-absolute bg-white shadow_box p-4">
							<h5>
								<a href="#">How to train your dragon</a>
							</h5>
							<h6 class="font_14 mt-3">
								<i class="fa fa-tag col_oran me-1"></i> Adventure <i
									class="fa fa-clock-o col_oran me-1 ms-3"></i> 120 Mins
							</h6>
							<ul class="mb-0 mt-3 font_14">
								<li class="d-inline-block"><a href="#"
									class="watch-trailer-btn" data-video="Dragon.mp4">Movie
										Details</a></li>
								<li class="d-inline-block ms-2"><a href="#">Get Ticket</a></li>
							</ul>
						</div>
					</div>
				</div>

				<!-- Movie 2 -->
				<div class="col-md-4">
					<div class="feat_2i position-relative">
						<div class="feat_2i1">
							<div class="grid clearfix">
								<figure class="effect-jazz mb-0">
									<a href="#"><img src="resources/cusTemplate/img/SonicPoster.jpg"
										class="w-100" alt="abc"></a>
								</figure>
							</div>
						</div>
						<div class="feat_2i2 position-absolute bg-white shadow_box p-4">
							<h5>
								<a href="#">Sonic 3</a>
							</h5>
							<h6 class="font_14 mt-3">
								<i class="fa fa-tag col_oran me-1"></i> Fantasy <i
									class="fa fa-clock-o col_oran me-1 ms-3"></i> 150 Mins
							</h6>
							<ul class="mb-0 mt-3 font_14">
								<li class="d-inline-block"><a href="#"
									class="watch-trailer-btn" data-video="Sonic3.mp4">Movie
										Details</a></li>
								<li class="d-inline-block ms-2"><a href="#">Get Ticket</a></li>
							</ul>
						</div>
					</div>
				</div>

				<!-- Movie 3 -->
				<div class="col-md-4">
					<div class="feat_2i position-relative">
						<div class="feat_2i1">
							<div class="grid clearfix">
								<figure class="effect-jazz mb-0">
									<a href="#"><img src="resources/cusTemplate/img/AlienPoster.jpg"
										class="w-100" alt="abc"></a>
								</figure>
							</div>
						</div>
						<div class="feat_2i2 position-absolute bg-white shadow_box p-4">
							<h5>
								<a href="#">Alien</a>
							</h5>
							<h6 class="font_14 mt-3">
								<i class="fa fa-tag col_oran me-1"></i> Thriller <i
									class="fa fa-clock-o col_oran me-1 ms-3"></i> 160 Mins
							</h6>
							<ul class="mb-0 mt-3 font_14">
								<li class="d-inline-block"><a href="#"
									class="watch-trailer-btn" data-video="Alien.mp4">Movie
										Details</a></li>
								<li class="d-inline-block ms-2"><a href="#">Get Ticket</a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>

			<div class="row feat_3 border_1 p-3 mx-auto"></div>
		</div>
	</section>

	<section id="feat" class="p_3 bg-light">
		<div class="container-xl">
			<div class="row feat_1 mb-4">
				<div class="col-md-6">
					<div class="feat_1l">
						<span class="fa fa-film col_oran"></span>
						<h6 class="text-muted mt-3">Checkout New Movies</h6>
						<h1 class="mb-0 font_50">Movie Now Showing!</h1>
					</div>
				</div>
				<div class="col-md-6">
					<div class="feat_1r mt-5">
						<p class="mb-0">You can watch movie trailer here!</p>
					</div>
				</div>
			</div>

			<div class="row feat_2">
				<!-- Movie 1 -->
				<div class="col-md-4">
					<div class="feat_2i position-relative">
						<div class="feat_2i1">
							<div class="grid clearfix">
								<figure class="effect-jazz mb-0">
									<a href="#"><img src="resources/cusTemplate/img/Avatar1.jpg"
										class="w-100" alt="abc"></a>
								</figure>
							</div>
						</div>
						<div class="feat_2i2 position-absolute bg-white shadow_box p-4">
							<h5>
								<a href="#">Avatar Fire & Ash</a>
							</h5>
							<h6 class="font_14 mt-3">
								<i class="fa fa-tag col_oran me-1"></i> Adventure <i
									class="fa fa-clock-o col_oran me-1 ms-3"></i> 172 Mins
							</h6>
							<ul class="mb-0 mt-3 font_14">
								<li class="d-inline-block"><a href="#"
									class="watch-trailer-btn" data-video="Avatar3.mp4">Movie
										Details</a></li>
								<li class="d-inline-block ms-2"><a href="#">Get Ticket</a></li>
							</ul>
						</div>
					</div>
				</div>

				<!-- Movie 2 -->
				<div class="col-md-4">
					<div class="feat_2i position-relative">
						<div class="feat_2i1">
							<div class="grid clearfix">
								<figure class="effect-jazz mb-0">
									<a href="#"><img src="resources/cusTemplate/img/deadpool.jpg"
										class="w-100" alt="abc"></a>
								</figure>
							</div>
						</div>
						<div class="feat_2i2 position-absolute bg-white shadow_box p-4">
							<h5>
								<a href="#">Deadpool</a>
							</h5>
							<h6 class="font_14 mt-3">
								<i class="fa fa-tag col_oran me-1"></i> Action <i
									class="fa fa-clock-o col_oran me-1 ms-3"></i> 128 Mins
							</h6>
							<ul class="mb-0 mt-3 font_14">
								<li class="d-inline-block"><a href="#"
									class="watch-trailer-btn" data-video="Deadpool.mp4">Movie
										Details</a></li>
								<li class="d-inline-block ms-2"><a href="#">Get Ticket</a></li>
							</ul>
						</div>
					</div>
				</div>

				<!-- Movie 3 -->
				<div class="col-md-4">
					<div class="feat_2i position-relative">
						<div class="feat_2i1">
							<div class="grid clearfix">
								<figure class="effect-jazz mb-0">
									<a href="#"><img src="resources/cusTemplate/img/Dune3.jpg"
										class="w-100" alt="abc"></a>
								</figure>
							</div>
						</div>
						<div class="feat_2i2 position-absolute bg-white shadow_box p-4">
							<h5>
								<a href="#">Dune Part Two</a>
							</h5>
							<h6 class="font_14 mt-3">
								<i class="fa fa-tag col_oran me-1"></i> Sci-fi <i
									class="fa fa-clock-o col_oran me-1 ms-3"></i> 168 Mins
							</h6>
							<ul class="mb-0 mt-3 font_14">
								<li class="d-inline-block"><a href="#"
									class="watch-trailer-btn" data-video="Dune2.mp4">Movie
										Details</a></li>
								<li class="d-inline-block ms-2"><a href="#">Get Ticket</a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>

			<div class="row feat_3 border_1 p-3 mx-auto"></div>
		</div>
	</section>

	<!-- Video Modal -->
	<div class="modal fade" id="templateVideoModal" tabindex="-1"
		aria-labelledby="videoModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-lg modal-dialog-centered">
			<div class="modal-content">
				<div class="modal-header">
					<h2 class="modal-title" id="videoTitle">Movie Trailer</h2>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">

					<!-- Movie Information -->
					<div class="movie-info mb-3">
						<h4 id="movieTitle"></h4>
						<p>
							<strong>Duration:</strong> <span id="movieDuration"></span>
						</p>
						<p>
							<strong>Categories:</strong> <span id="movieCategories"></span>
						</p>
						<p>
							<strong>Cast:</strong> <span id="movieCast"></span>
						</p>
						<p>
							<strong>Description:</strong>
						</p>
						<p id="movieDescription"></p>
					</div>

					<!-- Video -->
					<div class="ratio ratio-16x9">
						<video id="localVideo" class="w-100" controls>
							<source id="videoSource" src="" type="video/mp4">
						</video>
					</div>

				</div>
			</div>
		</div>
	</div>

	<jsp:include page="../cusFooter.jsp"></jsp:include>

	<!-- JavaScript to Handle Trailer Playback -->
	<script>
document.addEventListener('DOMContentLoaded', function () {
	  var templateVideoModal = document.getElementById('templateVideoModal');
	  var localVideo = document.getElementById('localVideo');
	  var videoSource = document.getElementById('videoSource');

	  var movieTitle = document.getElementById('movieTitle');
	  var movieDuration = document.getElementById('movieDuration');
	  var movieCategories = document.getElementById('movieCategories');
	  var movieCast = document.getElementById('movieCast');
	  var movieDescription = document.getElementById('movieDescription');

	  document.querySelectorAll('.watch-trailer-btn').forEach(button => {
	    button.addEventListener('click', function (event) {
	      event.preventDefault();

	      var videoFile = this.getAttribute('data-video'); // Get the video file name

	      if (movies.hasOwnProperty(videoFile)) {
	        var movieData = movies[videoFile];

	        // Update Movie Details
	        movieTitle.textContent = movieData.title;
	        movieDuration.textContent = movieData.duration;
	        movieCategories.textContent = movieData.categories;
	        movieCast.textContent = movieData.cast;
	        movieDescription.textContent = movieData.description;

	        // Set Video Source and Play
	        videoSource.src = movieData.videoPath;
	        localVideo.load();  // Reload the video file
	        localVideo.play();  // Auto-play the video
	      } else {
	        console.error("Movie not found:", videoFile);
	      }

	      // Show Modal
	      var modal = new bootstrap.Modal(templateVideoModal);
	      modal.show();
	    });
	  });

	  // Pause and reset video when modal closes
	  templateVideoModal.addEventListener('hide.bs.modal', function () {
	    localVideo.pause();
	    localVideo.currentTime = 0;
	  });
	});

    var movies = {
  "Dragon.mp4": {
    title: "How to Train Your Dragon",
    duration: "120 Mins",
    categories: "Adventure, Fantasy",
    cast: "Jay Baruchel, America Ferrera, Gerard Butler",
    description: "A young Viking befriends a dragon, changing the fate of his people and the mythical creatures they once feared.",
    videoPath: "resources/cusTemplate/videos/Dragon.mp4"
  },
  "Sonic3.mp4": {
    title: "Sonic 3",
    duration: "150 Mins",
    categories: "Fantasy, Action",
    cast: "Ben Schwartz, Jim Carrey, Idris Elba",
    description: "Sonic and his friends face their greatest challenge yet as they battle against powerful new threats in an action-packed adventure.",
    videoPath: "resources/cusTemplate/videos/Sonic3.mp4"
  },
  "Alien.mp4": {
    title: "Alien: Romulus",
    duration: "160 Mins",
    categories: "Thriller, Horror, Sci-Fi",
    cast: "Cailee Spaeny, Isabela Merced, Archie Renaux",
    description: "A new chapter in the Alien saga, where a group of young explorers uncover a terrifying extraterrestrial threat on a distant colony.",
    videoPath: "resources/cusTemplate/videos/Alien.mp4"
  },
  "Avatar3.mp4": {
    title: "Avatar Fire & Ash",
    duration: "172 Mins",
    categories: "Adventure",
    cast: "Sam Worthington, Zoe Saldana",
    description: "An epic journey in the Avatar universe.",
    videoPath: "resources/cusTemplate/videos/Avatar3.mp4"
  },
  "Deadpool.mp4": {
    title: "Deadpool & Wolverine",
    duration: "128 Mins",
    categories: "Action",
    cast: "Ryan Reynolds, Hugh Jackman",
    description: "The Merc with a Mouth returns for more chaos.",
    videoPath: "resources/cusTemplate/videos/Deadpool.mp4"
  },
  "Dune2.mp4": {
    title: "Dune Part Two",
    duration: "168 Mins",
    categories: "Sci-fi",
    cast: "Timothée Chalamet, Zendaya",
    description: "Paul Atreides unites with the Fremen.",
    videoPath: "resources/cusTemplate/videos/Dune2.mp4"
  }
};
</script>

<!-- 	<script src="js/common.js"></script> -->

</body>

</html> --%>