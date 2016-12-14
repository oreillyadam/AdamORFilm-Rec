package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Objects;

import utils.Validator;

public class Film implements Comparable<Film> {

	private Long id;
	private String title;
	private String year;
	private String url;

	private Map<Long, Rating> ratings = new TreeMap<Long, Rating>();
	private ArrayList<String> categories = new ArrayList<String>();
	private double stars;

	// /////////////////////////////////////////////////////////////////////////////////////
	// constructer
	public Film(Long id, String title, String year, String url) {
		this.id = id;
		this.title = title;
		this.year = year;
		this.url = url;
		ratings = new HashMap<Long, Rating>();
	}

	// the following are the standard getters and setters
	// /////////////////////////////////////////////////////////////////////////////////////

	public String getTitle() {
		return title;
	}

	public String getYear() {
		return year;
	}

	public String getUrl() {
		return url;
	}

	public Map<Long, Rating> getRatings() {
		return ratings;
	}

	public ArrayList<String> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}

	public Long getId() {
		return id;
	}

	public double getStars() {
		if (ratings.size() != 0) {
			double stars = 0;
			for (Rating rating : ratings.values()) {
				stars += rating.getStars();
			}
			return stars / ratings.size();
		} else {

			return 0;
		}
	}

	public void setStars(double stars) {
		this.stars = stars;
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Film) {
			final Film other = (Film) obj;
			return Objects.equal(id, other.id)
					&& Objects.equal(title, other.title)
					&& Objects.equal(year, other.year)
					&& Objects.equal(url, other.url)
					&& Objects.equal(ratings, other.ratings)
					&& Objects.equal(categories, other.categories)
					&& Objects.equal(stars, other.stars);
		} else {
			return false;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// to string method
	public String toString() {
		String movieRatings = "";
		if (ratings.size() > 0) {
			movieRatings += "(" + Validator.toTwoDecimalPlaces(getStars())
					+ ")";
		} else {
			movieRatings = " (not rated yet)";
		}
		return title + movieRatings;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// comparator
	@Override
	public int compareTo(Film other) {
		if (this.getStars() < other.getStars())
			return -1;
		if (this.getStars() > other.getStars())
			return 1;
		return 0;
	}
}
