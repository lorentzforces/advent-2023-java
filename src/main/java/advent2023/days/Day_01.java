package advent2023.days;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;

public class Day_01 {

	private static final Map<String, Character> DIGIT_WORDS;
	private static final Pattern DIGIT_PATTERN;

	static {
		DIGIT_WORDS = new HashMap<>();
		DIGIT_WORDS.put("zero", '0');
		DIGIT_WORDS.put("one", '1');
		DIGIT_WORDS.put("two", '2');
		DIGIT_WORDS.put("three", '3');
		DIGIT_WORDS.put("four", '4');
		DIGIT_WORDS.put("five", '5');
		DIGIT_WORDS.put("six", '6');
		DIGIT_WORDS.put("seven", '7');
		DIGIT_WORDS.put("eight", '8');
		DIGIT_WORDS.put("nine", '9');

		DIGIT_PATTERN = Pattern.compile(
			DIGIT_WORDS.entrySet().stream()
				.flatMap(entry -> Stream.of(entry.getKey(), String.valueOf(entry.getValue())))
				.collect(Collectors.joining("|"))
		);
	}

	public static Integer part_01(@NonNull BufferedReader input) {
		return input.lines().map(Day_01::calibrationValueLiteralDigits).reduce(Integer::sum).get();
	}

	public static Integer part_02(@NonNull BufferedReader input) {
		return input.lines().map(Day_01::calibrationValueDigitsAndWords).reduce(Integer::sum).get();
	}

	private static Integer calibrationValueLiteralDigits(@NonNull String line) {
		Character firstDigit = null;
		Character lastDigit = null;

		for (var i = 0; i < line.length(); i++) {
			final var c = line.charAt(i);
			if (Character.isDigit(c)) {
				if (firstDigit == null) {
					firstDigit = c;
				}
				lastDigit = c;
			}
		}

		if (firstDigit == null || lastDigit == null) {
			throw new RuntimeException(String.format(
				"Line \"%s\" did not contain two digit characters", line
			));
		}

		return Integer.valueOf(String.valueOf(firstDigit) + lastDigit);
	}

	protected static Integer calibrationValueDigitsAndWords(@NonNull String line) {
		Character firstDigit = null;
		Character lastDigit = null;

		final var matcher = DIGIT_PATTERN.matcher(line);
		while (matcher.find()) {
			final var word = matcher.group();
			final var c = switch (word.length()) {
				case 1: yield word.charAt(0);
				default: yield DIGIT_WORDS.get(word);
			};

			if (firstDigit == null) {
				firstDigit = c;
			}
			lastDigit = c;

			// only advance the matcher one character per match, in case we have digit words which
			// overlap each other, like "twone"
			matcher.region(matcher.start() + 1, line.length());
		}

		return Integer.valueOf(String.valueOf(firstDigit) + lastDigit);
	}

}
