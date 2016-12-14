package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.introcs.In;
import models.Film;
import models.Rating;
import models.User;

public class CSVLoader {

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<User> loadUsers(String filename) throws Exception {
		File usersFile = new File(filename);
		if (usersFile.exists()) {
			List<User> users = new ArrayList<User>();
			In inUsers = new In(usersFile);
			String seperate = "[|]";
			int counter = 1;
			while (!inUsers.isEmpty()) {
				String password = "secret";
				String userDetails = inUsers.readLine();

				String[] userTokens = userDetails.split(seperate);

				if (userTokens.length == 7) {
					users.add(new User(Long.valueOf(userTokens[0]),
							userTokens[1], userTokens[2], Integer
									.parseInt(userTokens[3]), userTokens[4],
							userTokens[5], "placeholder" + counter
									+ "@email.ie", password));
				}
				counter++;
			}
			System.out.println(users.size() + "uses imported");
			return users;
		} else {
			return null;
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Film> loadMovies(String filmFilename, String genreFilename)
			throws Exception {
		File filmFile = new File(filmFilename);
		File genresFile = new File(filmFilename);
		if (filmFile.exists() && genresFile.exists()) {
			String seperate = "[|]";
			String[] genres = new String[19];
			int counter = 0;

			In inGenres = new In(genresFile);
			while (!inGenres.isEmpty()) {
				String genreDetails = inGenres.readLine();

				String[] genreTokens = genreDetails.split(seperate);

				// pipe genre data to the console window.
				if (genreTokens.length == 2) {
					genres[counter] = genreTokens[0];
				}
			}

			List<Film> movies = new ArrayList<Film>();
			counter = 1;

			In inMovies = new In(filmFile);
			while (!inMovies.isEmpty()) {
				String movieDetails = inMovies.readLine();

				// parse the film details
				String[] filmTokens = movieDetails.split(seperate);

				// pipe film data to the console window.
				if (filmTokens.length >= 4) {
 					movies.add(new Film((long) counter, filmTokens[1],
							filmTokens[2], filmTokens[3]));
					 
				}
				counter++;
			}
			System.out.println(movies.size() + " movies imported");
			return movies;
		} else {
			return null;
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<Rating> loadRatings(String filename) throws Exception {
		File ratingsFile = new File(filename);
		if (ratingsFile.exists()) {
			List<Rating> ratings = new ArrayList<Rating>();
			In inRatings = new In(ratingsFile);
			String seperate = "[|]";
			while (!inRatings.isEmpty()) {
				// get rating details
				String ratingDetails = inRatings.readLine();

				// parse rating details
				String[] ratingTokens = ratingDetails.split(seperate);

				// output rating data to console.
				if (ratingTokens.length == 4) {
					ratings.add(new Rating(Long.valueOf(ratingTokens[0]), Long
							.valueOf(ratingTokens[1]), Integer
							.parseInt(ratingTokens[2])));
				}
			}
			System.out.println(ratings.size() + "ratings imported ");
			return ratings;
		} else {
			return null;
		}
	}
}
