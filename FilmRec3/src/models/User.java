package models;

import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Objects;

public class User  {
	private Long id;
	private String firstName;
	private String lastName;
	private int age;
	private String gender;
	private String occupation;
	private String email;
	private String password;

	private Map<Long, Rating> ratings = new TreeMap<Long, Rating>();

	// /////////////////////////////////////////////////////////////////////////////////////
	// constructor
	public User(Long id, String firstName, String lastName, int age,
			String gender, String occupation, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		if (gender.equalsIgnoreCase("f") || gender.equalsIgnoreCase("female")) {
			this.gender = "female";
		} else {
			this.gender = "male";
		}
		if (age > 0 && age < 115) {
			this.age = age;
		} else {
			this.age = 20;
		}
		this.occupation = occupation;
		this.email = email;
		this.password = password;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// getters and setters
	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getAge() {
		return age;
	}

	public String getGender() {
		return gender;
	}

	public String getOccupation() {
		return occupation;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	@Override
	public int hashCode() {
		return Objects.hashCode(this.id, this.firstName, this.lastName,
				this.age, this.gender, this.occupation, this.email,
				this.password);
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof User) {
			final User other = (User) obj;
			return Objects.equal(id, other.id)
					&& Objects.equal(firstName, other.firstName)
					&& Objects.equal(lastName, other.lastName)
					&& Objects.equal(age, other.age)
					&& Objects.equal(gender, other.gender)
					&& Objects.equal(occupation, other.occupation)
					&& Objects.equal(email, other.email)
					&& Objects.equal(password, other.password)
					&& Objects.equal(ratings, other.ratings);
		} else {
			return false;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	public Map<Long, Rating> getRatings() {
		return ratings;
	}

	public void removeRatings() {
		ratings.clear();
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	// to construter
	public String toString() {
		String userRatings = "";
		if (ratings.size() > 0) {
			userRatings += "\nratings:\n";
			for (Rating rating : ratings.values()) {
				userRatings += "\tmovie id:" + rating.getMovieID() + "\t stars:"
						+ rating.getStars() + "\n";
			}
		} else {
			userRatings += " (no movies rated yet)";
		}
		return firstName + " " + lastName + userRatings;

	}

	// /////////////////////////////////////////////////////////////////////////////////////

	
}
