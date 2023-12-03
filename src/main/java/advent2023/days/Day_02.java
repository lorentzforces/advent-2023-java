package advent2023.days;

import java.io.BufferedReader;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.NonNull;

public class Day_02 {

	public static Integer part_01(@NonNull BufferedReader input) {
		final var checkCount = BagSample.builder().red(12).green(13).blue(14).build();
		return input.lines()
			.map(Day_02::parseGameLine)
			.filter(game -> game.isPossibleWithTotalCount(checkCount))
			.map(game -> game.index)
			.reduce(Integer::sum)
			.get();
	}

	public static Integer part_02(@NonNull BufferedReader input) {
		return input.lines()
			.map(Day_02::parseGameLine)
			.map(game -> game.powerValue)
			.reduce(Integer::sum)
			.get();
	}

	@Builder
	public static record BagSample(
		int red,
		int green,
		int blue
	) {}

	public static class BagGame {
		public final int index;
		public final int maxRed;
		public final int maxGreen;
		public final int maxBlue;
		public final int powerValue;
		public final Set<BagSample> samples;

		public BagGame(int index, @NonNull Set<BagSample> samples) {
			this.index = index;
			this.samples = samples;
			int red = 0, green = 0, blue = 0;

			for (var sample : samples) {
				red = sample.red > red ? sample.red : red;
				green = sample.green > green ? sample.green : green;
				blue = sample.blue > blue ? sample.blue : blue;
			}

			maxRed = red;
			maxGreen = green;
			maxBlue = blue;

			powerValue = maxRed * maxGreen * maxBlue;
		}

		public boolean isPossibleWithTotalCount(BagSample totalCounts) {
			return totalCounts.red >= maxRed
				&& totalCounts.green >= maxGreen
				&& totalCounts.blue >= maxBlue;
		}
	}

	// as usual, we assume our input is syntactically correct
	public static BagGame parseGameLine(@NonNull String line) {
		final var labelAndSamples = line.split(":");

		final var gameIndex = Integer.valueOf(labelAndSamples[0].split(" ")[1]);

		final var bagSamples =
			Stream.of(labelAndSamples[1].split(";"))
			.map(Day_02::parseSample)
			.collect(Collectors.toSet());

		return new BagGame(gameIndex, bagSamples);
	}

	private static BagSample parseSample(@NonNull String text) {
		int red = 0, green = 0, blue = 0;

		for (var amountText : text.split(",")) {
			final var components = amountText.trim().split(" ");
			final var amount = Integer.valueOf(components[0]);
			switch (components[1]) {
				case "red": red = amount; break;
				case "green": green = amount; break;
				case "blue": blue = amount; break;
				default: throw new RuntimeException(String.format(
						"Expected color, found: \"%s\"", components[1]
					));
			}
		}

		return BagSample.builder().red(red).green(green).blue(blue).build();
	}

}

