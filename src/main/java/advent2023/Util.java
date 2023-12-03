package advent2023;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;

public class Util {
	public static BufferedReader readResource(String resourcePath) {
		return new BufferedReader(new InputStreamReader(
			Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)
		));
	}

	public static BufferedReader readerFromString(String s) {
		return new BufferedReader(new StringReader(s));
	}
}
