package advent2023.days;

import static advent2023.Util.readerFromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import advent2023.days.Day_02.BagSample;
import org.junit.jupiter.api.Test;

public class Day_02_Test {

	private static final String sampleInput = """
		Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
		Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
		Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
		Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
		Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green""";

	@Test
	public void parseGameLineProducesExpected() {
		final var line = "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green";
		final var game = Day_02.parseGameLine(line);

		assertThat(game.index, is(5));
		assertThat(game.samples, hasSize(2));
		assertThat(game.samples, hasItem(BagSample.builder().red(6).blue(1).green(3).build()));
		assertThat(game.samples, hasItem(BagSample.builder().blue(2).red(1).green(2).build()));
	}

	@Test
	public void part1SampleInputProducesExpected() {
		final var result = Day_02.part_01(readerFromString(sampleInput));
		assertThat(result, is(8));
	}

	@Test
	public void part2SampleInputProducesExpected() {
		final var result = Day_02.part_02(readerFromString(sampleInput));
		assertThat(result, is(2286));
	}

}
