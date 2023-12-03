package advent2023;

import advent2023.days.Day_01;

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
			System.out.println(String.format(
				"Result for Day %2d [Part %2d]: %s",
				day,
				part,
				runFunction.apply(Util.readResource(inputFile))
			));
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
			.build()
	);
}
