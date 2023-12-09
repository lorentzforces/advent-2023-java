package advent2023.days;

import static advent2023.Util.readLineFullSend;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.NonNull;

public class Day_06 {

	public static Long part_01(@NonNull BufferedReader input) {
		final var races = parseRaces(input);
		return races.stream().map(Day_06::calculateWinCount).reduce((a, b) -> a * b).get();
	}

	public static Long part_02(@NonNull BufferedReader input) {
		final var race = parseSingleRace(input);
		return calculateWinCount(race);
	}

	private static List<Race> parseRaces(@NonNull BufferedReader input) {
		final var timeLine = readLineFullSend(input);
		final var distanceLine = readLineFullSend(input);
		final var times =
			Stream.of(timeLine.split("\\:")[1].trim().split("\\s+"))
			.map(Long::valueOf)
			.toList();
		final var distances =
			Stream.of(distanceLine.split("\\:")[1].trim().split("\\s+"))
			.map(Long::valueOf)
			.toList();
		if (distances.size() != times.size()) {
			throw new RuntimeException(String.format(
				"Expected times and distances to have matched elements, but got %d distances and "
					+ "%d times.",
				distances.size(),
				times.size()
			));
		}

		final var races = new ArrayList<Race>(distances.size());
		for (var i = 0; i < distances.size(); i++) {
			races.add(
				Race.builder()
					.duration(times.get(i))
					.recordDistance(distances.get(i))
					.build()
			);
		}

		return races;
	}

	/**
	 * The part 2 parsing, which ignores all whitespace between numbers to produce a single race.
	 */
	private static Race parseSingleRace(@NonNull BufferedReader input) {
		final var timeLine = readLineFullSend(input);
		final var distanceLine = readLineFullSend(input);
		final var time = Long.valueOf(timeLine.split("\\:")[1].replaceAll("\\s+", ""));
		final var distance = Long.valueOf(distanceLine.split("\\:")[1].replaceAll("\\s+", ""));
		return Race.builder().duration(time).recordDistance(distance).build();
	}

	/**
	 * The highest integer on the number line which is strictly less than the provided double value.
	 */
	private static long highestWholeNumber(double x) {
		assert x > 0;
		final var longValue = (long)x;
		return longValue == x ? longValue - 1 : longValue;
	}

	private static long calculateWinCount(@NonNull Race race) {
		final var roots = quadratic(1L, -1 * race.duration(), race.recordDistance());
		// We do this weird dance to cover whole integer values. We want to know how many whole
		// integer numbers are on the number line between our two roots. If our roots are, for
		// example, 10.0 and 20.0, there are 9 whole numbers in that (exclusive) range. We find the
		// largest integer strictly less than the upper bound, and just truncate the lower bound.
		return highestWholeNumber(roots[0]) - (long)roots[1];
	}

	/**
	 * Returns the roots of the equation 0 = ax^2 + bx + c in a 2-element array
	 */
	private static double[] quadratic(long a, long b, long c) {
		final double A = (double)a, B = (double)b, C = (double)c;
		// this allows us to re-use values a bit more than the standard quadratic equation
		// see: https://en.wikipedia.org/wiki/Quadratic_formula#Equivalent_formulations
		final var flippableValue = Math.sqrt(Math.pow(B / (A * 2), 2) - (C / A));
		final var constValue = -1 * B / (A * 2);
		return new double[]{constValue + flippableValue, constValue - flippableValue};
	}

	@Builder
	public static record Race (
		long duration,
		long recordDistance
	) {}

}
