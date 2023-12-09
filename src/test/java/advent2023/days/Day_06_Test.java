package advent2023.days;

import static advent2023.Util.readerFromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import org.junit.jupiter.api.Test;

public class Day_06_Test {

	private static final String sampleInput = """
		Time:      7  15   30
		Distance:  9  40  200""";

	@Test
	public void part1SampleInputProducesExpected() throws IOException {
		final var result = Day_06.part_01(readerFromString(sampleInput));
		assertThat(result, is(288L));
	}

	@Test
	public void part2SampleInputProducesExpected() throws IOException {
		final var result = Day_06.part_02(readerFromString(sampleInput));
		assertThat(result, is(71503L));
	}

}
