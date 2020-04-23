package pl.klakier.flappybird.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {

	private FileUtils() {
	}

	public static String loadAsString(String path) {
		StringBuilder result = new StringBuilder();
		try {
			String buffer = new String();
			BufferedReader reader = new BufferedReader(new FileReader(path));
			while((buffer = reader.readLine()) != null) {
				result.append(buffer + '\n');
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result.toString();
	}
	
}
