package utils;

/**
 * 
 * @author Eamonn DeLeastar taken from Moodle notes 
 *
 */
public interface Serializer {

	void push(Object o);

	Object pop();

	void write() throws Exception;

	void read() throws Exception;
}