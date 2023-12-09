package advent2023.days;

import static advent2023.Util.readerFromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import advent2023.days.Day_07.Hand;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class Day_07_Test {

	private static final String sampleInput = """
		32T3K 765
		T55J5 684
		KK677 28
		KTJJT 220
		QQQJA 483""";

	@Test
	public void part1SampleInputProducesExpected() throws IOException {
		final var result = Day_07.part_01(readerFromString(sampleInput));
		assertThat(result, is(6440));
	}

	@Test
	public void part2SampleInputProducesExpected() throws IOException {
		final var result = Day_07.part_02(readerFromString(sampleInput));
		assertThat(result, is(5905));
	}

	@Test
	public void wildJacksSortLowerForFiveKind() {
		final var allJacks = Hand.withWildCards("JJJJJ", 1);
		final var allKings = Hand.withWildCards("22222", 1);
		assertThat(allKings, greaterThan(allJacks));
	}
}
