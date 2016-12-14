package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.TreeMap;

import models.Film;
import models.Rating;
import models.User;
import utils.CSVLoader;

import utils.Serializer;

public class FilmRecommenderAPI {

	private Map<Long, User> usersIndex = new TreeMap<Long, User>();
	private Map<String, User> usersEmails = new HashMap<String, User>();
	private Map<Long, Film> movies = new TreeMap<Long, Film>();
	private Serializer serializer;
	private boolean loggedIn;
	private Long loggedInID;
	private User admin;

	// /////////////////////////////////////////////////////////////////////////////////////

	public FilmRecommenderAPI(Serializer serializer) {
		this.serializer = serializer;
		createAdmin();
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public FilmRecommenderAPI() {
		createAdmin();
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	// makes the admiins master class
	public void createAdmin() {
		admin = new User(0l, "Adam", "O Reilly", 20, "male", "student",
				"adam@gmail.com", "password");
		usersIndex.put(admin.getId(), admin);
		usersEmails.put(admin.getEmail(), admin);
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public void addUser(String firstName, String lastName, String gender,
			int age, String occupation, String email, String password) {
		User user;
		if (usersIndex.size() > 1) {
			user = new User((((TreeMap<Long, User>) usersIndex).lastKey() + 1),
					firstName, lastName, age, gender, occupation, email,
					password);
		} // end of if block
		else {
			user = new User(1l, firstName, lastName, age, gender, occupation,
					email, password);
		}// end of else block

		usersIndex.put(user.getId(), user);
		usersEmails.put(email, user);

	}// end of method

	// ///

	// /////////////////////////////////////////////////////////////////////////////////////

	public void addUser(Long id, String firstName, String lastName, int age,
			String gender, String occupation, String email, String password) {
		if (usersIndex.size() > 1) {
			if (id != (((TreeMap<Long, User>) usersIndex).lastKey() + 1)) {
				id = (((TreeMap<Long, User>) usersIndex).lastKey() + 1);
			}
		} // end of if block
		else {
			id = 1l;
		} // end of else block
		User user = new User(id, firstName, lastName, age, gender, occupation,
				email, password);
		usersIndex.put(user.getId(), user);
		usersEmails.put(email, user);

	} // end of method

	// /////////////////////////////////////////////////////////////////////////////////////

	public User removeUser(long userID) {
		if (usersIndex.containsKey(userID)) {
			User user = usersIndex.remove(userID);
			usersEmails.remove(user.getEmail());
			for (Film movie : movies.values()) {
				movie.getRatings().remove(user.getId()); // removing user rating
			}
			if (getLoggedInID() != 0l) {
				logout();
			}
			return user;
		} else {
			return null;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public void addMovie(String title, String year, String url) {
		Long id;
		if (movies.size() == 0) {
			id = 1l;
		} // end of if
		else {
			id = (((TreeMap<Long, Film>) movies).lastKey() + 1);
		}// end of else
		Film movie = new Film(id, title, year, url);
		movies.put(movie.getId(), movie);
	} // end of method

	// /////////////////////////////////////////////////////////////////////////////////////

	public Film removeMovie(long movieID) {
		if (movies.containsKey(movieID)) {
			Film movie = movies.remove(movieID);

			return movie;
		} else {
			return null;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public void addRating(long userID, long movieID, int stars) {

		Rating rating = new Rating(userID, movieID, stars);
		usersIndex.get(userID).getRatings().put(movieID, rating);
		movies.get(movieID).getRatings().put(userID, rating);
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public Film getMovie(long movieID) {
		if (movies.containsKey(movieID)) {
			movies.get(movieID).setStars(movies.get(movieID).getStars());
			return movies.get(movieID);
		} else {
			return null;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public Map<Long, Rating> getUserRatings(long userID) {
		return getUsersIndices().get(userID).getRatings();
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public void load() throws Exception {

		serializer.read();
		movies = (Map<Long, Film>) serializer.pop();
		usersEmails = (Map<String, User>) serializer.pop();
		usersIndex = (Map<Long, User>) serializer.pop();
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public void write() throws Exception {
		serializer.push(usersIndex);
		serializer.push(usersEmails);
		serializer.push(movies);
		serializer.write();
	}

	// /////////////////////////////////////////////////////////////////////////////////////////////////////
	public void loadDAT() throws Exception {
		System.out.println("import dat files");
		CSVLoader loader = new CSVLoader();
		List<User> users = loader.loadUsers("data_movieLens/users.dat");
		for (User user : users) {
			usersIndex.put(user.getId(), user);
			usersEmails.put(user.getEmail(), user);
		}
		List<Film> movieList = loader.loadMovies("data_movieLens/items.dat",
				"data_movieLens/genres.dat");

		for (Film movie : movieList) {
			movies.put(movie.getId(), movie);
		}
		List<Rating> ratings = loader.loadRatings("data_movieLens/ratings.dat");
		for (Rating rating : ratings) {
			usersIndex.get(rating.getUserID()).getRatings()
					.put(rating.getMovieID(), rating);
			movies.get(rating.getMovieID()).getRatings()
					.put(rating.getUserID(), rating);
		}
		createAdmin();
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public void loadDAT(String usersPath, String moviesPath, String genresPath,
			String ratingsPath) throws Exception {
		CSVLoader loader = new CSVLoader();
		List<User> users = loader.loadUsers(usersPath);
		for (User user : users) {
			usersIndex.put(user.getId(), user);
			usersEmails.put(user.getEmail(), user);
		}
		List<Film> movieList = loader.loadMovies(moviesPath, genresPath);
		for (Film movie : movieList) {
			movies.put(movie.getId(), movie);
		}
		List<Rating> ratings = loader.loadRatings(ratingsPath);
		for (Rating rating : ratings) {
			usersIndex.get(rating.getUserID()).getRatings()
					.put(rating.getMovieID(), rating);
			movies.get(rating.getMovieID()).getRatings()
					.put(rating.getUserID(), rating);
		}
		createAdmin();
	}

	public TreeMap<Long, Film> findMovie(String title) {
		TreeMap<Long, Film> titles = new TreeMap<Long, Film>();
		for (Film movie : movies.values()) {
			if (movie.getTitle().toLowerCase().contains(title.toLowerCase())) {
				titles.put(movie.getId(), movie);
			}
		}
		return titles;
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public Map<Long, User> getUsersIndices() {
		return usersIndex;
	}

	public Map<String, User> getUsersEmails() {
		return usersEmails;
	}

	public Map<Long, Film> getMovies() {
		return movies;
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public boolean login(String email, String password) {
		if (usersEmails.containsKey(email)) {
			if (usersEmails.get(email).getPassword().equals(password)) {
				setLoggedIn(true);
				setLoggedInID(usersEmails.get(email).getId());
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public void logout() {
		setLoggedIn(false);
		setLoggedInID(-1l);
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public boolean isLoggedIn() {
		return loggedIn;
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public Long getLoggedInID() {
		return loggedInID;
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public void setLoggedInID(Long loggedInID) {
		this.loggedInID = loggedInID;
	}
}
