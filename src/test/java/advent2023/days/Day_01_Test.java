package advent2023.days;

import static advent2023.Util.readerFromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class Day_01_Test {

	private static final String part1SampleInput = """
		1abc2
		pqr3stu8vwx
		a1b2c3d4e5f
		treb7uchet""";

	@Test
	public void part1SampleInputProducesExpected() {
		final var result = Day_01.part_01(readerFromString(part1SampleInput));
		assertThat(result, is(142));
	}

	private static final String part2SampleInput = """
		two1nine
		eightwothree
		abcone2threexyz
		xtwone3four
		4nineeightseven2
		zoneight234
		7pqrstsixteen""";

	@Test
	public void part2SampleInputProducesExpected() {
		final var result = Day_01.part_02(readerFromString(part2SampleInput));
		assertThat(result, is(281));
	}

	@Test
	public void wordsAreTreatedAsDigits() {
		final var resultLine0 = Day_01.calibrationValueDigitsAndWords("two1nine");
		assertThat(resultLine0, is(29));
		final var resultLine1 = Day_01.calibrationValueDigitsAndWords("eightwothree");
		assertThat(resultLine1, is(83));
		final var resultLine2 = Day_01.calibrationValueDigitsAndWords("abcone2threexyz");
		assertThat(resultLine2, is(13));
		final var resultLine3 = Day_01.calibrationValueDigitsAndWords("xtwone3four");
		assertThat(resultLine3, is(24));
		final var resultLine4 = Day_01.calibrationValueDigitsAndWords("4nineeightseven2");
		assertThat(resultLine4, is(42));
		final var resultLine5 = Day_01.calibrationValueDigitsAndWords("zoneight234");
		assertThat(resultLine5, is(14));
		final var resultLine6 = Day_01.calibrationValueDigitsAndWords("7pqrstsixteen");
		assertThat(resultLine6, is(76));
	}

	@Test
	public void wordsWithinEachOtherAreBothCounted() {
		final var result = Day_01.calibrationValueDigitsAndWords("1twone");
		assertThat(result, is(11));
	}

}
