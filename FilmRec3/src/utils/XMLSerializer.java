package utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;

public class XMLSerializer implements Serializer {
	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("rawtypes")
	private Stack stack = new Stack();
	private File file;

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public XMLSerializer(File file) {
		this.file = file;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	public void push(Object o) {
		stack.push(o);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public Object pop() {
		return stack.pop();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("rawtypes")
	public void read() throws Exception {
		ObjectInputStream ois = null;

		try {
			XStream xstream = new XStream(new DomDriver());
			ois = xstream.createObjectInputStream(new FileReader(file));
			stack = (Stack) ois.readObject();
		} finally {
			if (ois != null) {
				ois.close();
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void write() throws Exception {
		ObjectOutputStream oos = null;

		try {
			XStream xstream = new XStream(new DomDriver());
			oos = xstream.createObjectOutputStream(new FileWriter(file));
			oos.writeObject(stack);
		} finally {
			if (oos != null) {
				oos.close();
			}
		}
	}
}