package controllers;

import java.io.File;
import java.util.TreeMap;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import models.Film;
import models.User;
import utils.Serializer;
import utils.Validator;
import utils.XMLSerializer;

public class Main {

	private FilmRecommenderAPI recommender;
	private Serializer serializer;

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Main() {
		File dataStore = new File("datastore.xml");
		serializer = new XMLSerializer(dataStore);
		recommender = new FilmRecommenderAPI(serializer);
		if (dataStore.isFile()) {
			try {
				System.out.println("Please wait while the data base is loaded");
				recommender.load();
			} catch (Exception e) {
				System.out.println("The database could not be loaded");
				e.printStackTrace();
			}
		} else {
			try {
				recommender.loadDAT();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		recommender.createAdmin();

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		Main client = new Main();

		Shell shell = ShellFactory
				.createConsoleShell(
						"System",
						"Welcome to java film recomender - type ?help for help",
						client);
		shell.commandLoop();

		client.recommender.write();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Command(description = "Register")
	public void Register(@Param(name = "first name") String firstName,
			@Param(name = "last name") String lastName,
			@Param(name = "age") int age,
			@Param(name = "gender") String gender,
			@Param(name = "occupation") String occupation,
			@Param(name = "email") String email,
			@Param(name = "password") String password) {
		if (!recommender.isLoggedIn()) {
			if (Validator.isValidEmailAddress(email)) {
				if (!recommender.getUsersEmails().containsKey(email)) {
					recommender.addUser(firstName, lastName, gender, age,
							occupation, email, password);

					recommender.login(email, password);

				} else {
					System.out.println("User with email: " + email
							+ " already exists in the database");
				}
			} else {
				System.out
						.println("Registration failed. Try puting in @ nd .com ");
			}
		} else {
			System.out.println("Log out to register another account");
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Command(description = "get all users")
	public void getUsers() {
		if (recommender.getUsersIndices().size() > 0) {
			for (User user : recommender.getUsersIndices().values()) {
				System.out.println(user.getId() + ": " + user.getFirstName()
						+ " " + user.getLastName());
			}
		} else {
			System.out.println("No users in the database");
		}
	}

	/**
	 * allows the logged in user to delete their account only
	 */
	@Command(description = "Delete your account")
	public void deleteYourAccount() {
		if (recommender.isLoggedIn()) {
			if (!(recommender.getLoggedInID() == 0l)) {
				recommender.removeUser(recommender.getLoggedInID());
			} else {
				System.out.println("administrator cann't remove their account");
			}
		} else {
			displayWarning();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Command(description = "Add movie")
	public void addMovie(@Param(name = "title") String title,
			@Param(name = "year") String year, @Param(name = "url") String url) {
		if (recommender.isLoggedIn()) {
			if (Validator.isValidDate(year)) {
				recommender.addMovie(title, year, url);
			} else {
				System.out
						.println("Invalid date format. Adding movie failed try putting a .com at the end of the URL"
								+ "or try the dateformat as dd/mm/yyyy");
			}
		} else {
			displayWarning();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Command(description = "remove film")
	public void removeMovie(@Param(name = "movieID") long movieID) {
		if (recommender.isLoggedIn()) {

			recommender.removeMovie(movieID);

		} else {
			displayWarning();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Command(description = "Add rating (only -5,-3,-1,1,3 or 5 allowed)")
	public void addRating(@Param(name = "movieID") Long movieId,
			@Param(name = "stars") int stars) {
		if (recommender.isLoggedIn()) {
			if (Validator.isValidRating(stars)
					&& recommender.getMovies().containsKey(movieId)) {
				recommender.addRating(recommender.getLoggedInID(), movieId,
						stars);
			} else {
				System.out
						.println("Adding a rating failed. Make sure movie name exists and your assainging stars "
								+ "witin accepted bounds ");
			}
		} else {
			displayWarning();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Command(description = "Get a Movie details")
	public void getMovie(@Param(name = "movieID") Long movieID) {
		if (recommender.getMovie(movieID) != null) {
			System.out.println(recommender.getMovie(movieID).toString());
		} else {
			System.out.println("Movie with supplied id is not in the database");
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Command(description = "get logged in user's ratings")
	public void getYourRatings() {
		if (recommender.isLoggedIn()) {
			long id = recommender.getLoggedInID();
			if (recommender.getUsersIndices().get(recommender.getLoggedInID())
					.getRatings().size() > 0) {
				System.out.println(recommender.getUsersIndices().get(id)
						.toString());
			} else {
				System.out.println("You have not rated any movies yet");
			}
		} else {
			displayWarning();
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Command(description = "Find a movie")
	public void findMovie(@Param(name = "title") String title) {
		TreeMap<Long, Film> movies = recommender.findMovie(title);
		if (movies.size() > 0) {
			System.out.println("Results for:" + title);
			for (Film movie : movies.values()) {
				System.out.println(movie.getId() + ": " + movie.getTitle());
			}
		} else {
			System.out.println("There is no movie with title" + title);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Command(description = "Log in")
	public void login(@Param(name = "email") String email,
			@Param(name = "password") String password) {
		if (!recommender.isLoggedIn()) {
			if (recommender.login(email, password)) {
				System.out.println("Hello "
						+ recommender.getUsersEmails().get(email)
								.getFirstName());
			} else {
				System.out
						.println("Incorrect email and/or password. Login failed");
			}
		} else {
			System.out
					.println("There is already a logged in user please log out first");
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Command(description = "logout")
	public void logout() {
		if (recommender.isLoggedIn()) {
			recommender.logout();
			System.out.println("Logout completed");
		} else {
			System.out.println("you are not loged in yet");
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////////
	@Command(description = "prime")
	public void loadPredefinedDatabase() {
		if (recommender.isLoggedIn() && recommender.getLoggedInID() == 0l) {
			try {
				recommender.loadDAT();
			} catch (Exception e) {
				System.out.println("The database did not load..");
			}
		} else {
			System.out.println("Only authorized users can load databases");
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// load .dat files

	@Command(description = "predifined databases ")
	public void loadPredefinedDatabase(
			@Param(name = "users file path") String usersPath,
			@Param(name = "movies file path") String moviesPath,
			@Param(name = "genres file path") String genresPath,
			@Param(name = "ratings file path") String ratingsPath) {
		if (recommender.isLoggedIn() && recommender.getLoggedInID() == 0l) {
			try {
				recommender.loadDAT(usersPath, moviesPath, genresPath,
						ratingsPath);
			} catch (Exception e) {
				System.out.println("The database was not loaded...");
			}
		} else {
			System.out.println("Only authorized users can load the database");
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	// used for xml file
	@Command(description = "load saved xml file")
	public void loadSavedDatabase() {
		if (recommender.isLoggedIn() && recommender.getLoggedInID() == 0l) {
			try {
				recommender.load();
				recommender.createAdmin();
			} catch (Exception e) {
				System.out.println("The database could not be loaded");
			}
		} else {
			System.out.println("Only authorized users can load the database");
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Command(description = "save the database")
	public void saveDatabase() throws Exception {
		if (recommender.isLoggedIn() && recommender.getLoggedInID() == 0l) {
			if (recommender.getMovies().size() > 0
					|| recommender.getUsersIndices().size() > 1) {
				try {
					recommender.write();
					System.out.println("Info: The database is being saved...");
				} catch (Exception e) {
					System.out.println("The database could not be saved");
				}
			} else {
				System.out.println("There is nothing to save...");
			}
		} else {
			System.out.println("Only authorized users can save the database");
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@Command(description = "clear the database")
	public void clearDatabase() {
		if (recommender.isLoggedIn() && recommender.getLoggedInID() == 0l) {
			File dataStore = new File("datastore.xml");
			serializer = new XMLSerializer(dataStore);
			recommender = new FilmRecommenderAPI(serializer);
			recommender.login(recommender.getUsersIndices().get(0l).getEmail(),
					recommender.getUsersIndices().get(0l).getPassword());
			System.out.println("The database has been cleared");
		} else {
			System.out.println("Only authorized users can clear the database");
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// displays a warning to someone who is not logged in when that info can
	// only be reached by an active account
	public void displayWarning() {
		System.out
				.println("you  are not permited to preform that action you must login first");
	}

}