package advent2023;

import advent2023.days.*;
import java.io.BufferedReader;
import java.util.List;
import java.util.function.Function;
import lombok.Builder;
import lombok.NonNull;

public class Main {

	public static void main(String[] args) {
		for (var puzzle : puzzleConfig) {
			puzzle.run();
		}
	}

	@Builder
	private static record AdventDayRun (
		int day,
		int part,
		String inputFile,
		@NonNull Function<BufferedReader, ? extends Object> runFunction
	) {
		public void run() {
			final var resultString = "Result for Day %2d [Part %2d]: %s";
			try (final var fileStream = Util.readResource(inputFile)) {
				final var result = runFunction.apply(Util.readResource(inputFile));
				System.out.println(String.format(
					resultString,
					day,
					part,
					result
				));
			} catch (Exception e) {
				System.out.println(String.format(
					resultString,
					day,
					part,
					" --ERROR-- " + e.getClass().getTypeName() + ": " + e.getMessage()
				));
			}
		}
	}

	private static List<AdventDayRun> puzzleConfig = List.of(
		AdventDayRun.builder()
			.day(1)
			.part(1)
			.inputFile("day-01.txt")
			.runFunction(Day_01::part_01)
			.build(),
		AdventDayRun.builder()
			.day(1)
			.part(2)
			.inputFile("day-01.txt")
			.runFunction(Day_01::part_02)
			.build(),
		AdventDayRun.builder()
			.day(2)
			.part(1)
			.inputFile("day-02.txt")
			.runFunction(Day_02::part_01)
			.build(),
		AdventDayRun.builder()
			.day(2)
			.part(2)
			.inputFile("day-02.txt")
			.runFunction(Day_02::part_02)
			.build(),
		AdventDayRun.builder()
			.day(3)
			.part(1)
			.inputFile("day-03.txt")
			.runFunction(Day_03::part_01)
			.build(),
		AdventDayRun.builder()
			.day(3)
			.part(2)
			.inputFile("day-03.txt")
			.runFunction(Day_03::part_02)
			.build(),
		AdventDayRun.builder()
			.day(4)
			.part(1)
			.inputFile("day-04.txt")
			.runFunction(Day_04::part_01)
			.build(),
		AdventDayRun.builder()
			.day(4)
			.part(2)
			.inputFile("day-04.txt")
			.runFunction(Day_04::part_02)
			.build(),
		AdventDayRun.builder()
			.day(5)
			.part(1)
			.inputFile("day-05.txt")
			.runFunction(Day_05::part_01)
			.build(),
		AdventDayRun.builder()
			.day(5)
			.part(2)
			.inputFile("day-05.txt")
			.runFunction(Day_05::part_02)
			.build(),
		AdventDayRun.builder()
			.day(6)
			.part(1)
			.inputFile("day-06.txt")
			.runFunction(Day_06::part_01)
			.build(),
		AdventDayRun.builder()
			.day(6)
			.part(2)
			.inputFile("day-06.txt")
			.runFunction(Day_06::part_02)
			.build(),
		AdventDayRun.builder()
			.day(7)
			.part(1)
			.inputFile("day-07.txt")
			.runFunction(Day_07::part_01)
			.build(),
		AdventDayRun.builder()
			.day(7)
			.part(2)
			.inputFile("day-07.txt")
			.runFunction(Day_07::part_02)
			.build()
	);
}
