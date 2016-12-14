package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Validator {

	// allowed rating values:
	public static final Integer[] SET_VALUES = new Integer[] { -5, -3, -1, 1,
			3, 5 };
	public static final Set<Integer> validRatings = new HashSet<Integer>(
			Arrays.asList(SET_VALUES));

	public static boolean isValidRating(int rating) {
		return (validRatings.contains(rating));
	}

	//the below email validation was sourced 
	//from http://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
	//for the sole pupose that i follows the RFC standard which is better than just the traditional method of hard coding
	//every possible input combination as java offers a package for it 
	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	public static double toTwoDecimalPlaces(double num) {
		return (int) (num * 100) / 100.0;
	}

	@SuppressWarnings("unused")
	public static boolean isValidDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
		try {
			Date test = sdf.parse(date);
			return true;
		} catch (ParseException pe) {
			return false;
		}

	}
}
