package models;

import com.google.common.base.Objects;

public class Rating {

	private Long userID;
	private Long movieID;
	private int stars;

	// /////////////////////////////////////////////////////////////////////////////////////
	// constructor
	public Rating(Long userID, Long movieID, int stars) {
		this.userID = userID;
		this.movieID = movieID;
		this.stars = stars;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// getters and setters
	public Long getUserID() {
		return userID;
	}

	public Long getMovieID() {
		return movieID;
	}

	public int getStars() {
		return stars;
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Rating) {
			final Rating other = (Rating) obj;
			return Objects.equal(userID, other.userID)
					&& Objects.equal(movieID, other.movieID)
					&& Objects.equal(stars, other.stars);
		} else {
			return false;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// to string
	public String toString() {
		return "\tuser id: " + userID + "\tmovie id: " + movieID + "\tstars: "
				+ stars;
	}
}
