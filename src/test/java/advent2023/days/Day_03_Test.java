package advent2023.days;

import static advent2023.Util.readerFromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class Day_03_Test {

	private static final String sampleInput = """
		467..114..
		...*......
		..35..633.
		......#...
		617*......
		.....+.58.
		..592.....
		......755.
		...$.*....
		.664.598..""";

	@Test
	public void part1SampleInputProducesExpected() {
		final var result = Day_03.part_01(readerFromString(sampleInput));
		assertThat(result, is(4361));
	}

	@Test
	public void part2SampleInputProducesExpected() {
		final var result = Day_03.part_02(readerFromString(sampleInput));
		assertThat(result, is(467835));
	}

}
