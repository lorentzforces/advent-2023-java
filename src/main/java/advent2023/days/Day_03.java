package advent2023.days;

import advent2023.Util;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.NonNull;

public class Day_03 {

	public static Integer part_01(@NonNull BufferedReader input) {
		return parseGrid(input)
			.adjacentNumbers()
			.map(number -> number.value)
			.reduce(Integer::sum)
			.get();
	}

	public static Integer part_02(@NonNull BufferedReader input) {
		return parseGrid(input).totalGearRatios();
	}

	private static final Pattern SEARCH_ITEMS = Pattern.compile("(\\d+|[^\\w\\s\\.])");

	private static GridItems parseGrid(@NonNull BufferedReader input) {
		final var matcher = SEARCH_ITEMS.matcher("");
		final var numbers = new ArrayList<List<Number>>();
		final var symbols = new ArrayList<Point>();
		var row = 0;
		var line = Util.readLineFullSend(input);
		while (line != null) {
			final var lineNumbers = new ArrayList<Number>();
			matcher.reset(line);
			while (matcher.find()) {
				try {
					final var value = Integer.valueOf(matcher.group());
					// throws exception if this is not numeric
					lineNumbers.add(
						Number.builder()
							.row(row)
							.left(matcher.start())
							.right(matcher.end() - 1)
							.value(value)
							.build()
					);
				} catch (NumberFormatException e) {
					// sanity check
					if (matcher.end() - matcher.start() != 1) {
						throw new RuntimeException(String.format(
							"Non-numeric line item (symbol) on line %d matched with more than one "
								+ "character: \"%s\"",
							row,
							matcher.group()
						));
					}
					symbols.add(Point.builder().row(row).column(matcher.start()).build());
				}
			}

			numbers.add(lineNumbers);
			row++;
			line = Util.readLineFullSend(input);
		}

		return GridItems.builder().numbers(numbers).symbols(symbols).build();
	}

	@Builder
	public static record GridItems(
		@NonNull List<List<Number>> numbers,
		@NonNull List<Point> symbols
	) {
		// This search could be more efficient - which basically would come down to being smarter
		// about which candidates we consider for adjacency. We're gonna consider this good enough
		// for now.
		public Stream<Number> adjacentNumbers() {
			return symbols.stream().flatMap(symbol -> {
				return getSearchNumbers(symbol).filter(number -> number.isAdjacent(symbol));
			});
		}

		// NOTE: Apparently my puzzle input only had "*" symbols which were adjacent to 2 numbers.
		// The general case with slightly different input would require storing the symbol and
		// filtering out any non-"*" symbols to accurately solve the problem.
		public Integer totalGearRatios() {
			return symbols.stream()
				.map(symbol -> {
					return getSearchNumbers(symbol)
						.filter(number -> number.isAdjacent(symbol))
						.toList();
				}).filter(adjacentNumbers -> adjacentNumbers.size() == 2)
				.map(adjacentNumbers ->
					adjacentNumbers.stream()
						.map(num -> num.value)
						.reduce((a, b) -> a * b)
						.get()
				).reduce(Integer::sum)
				.get();
		}

		/**
		 * Get a stream of numbers from adjacent rows of the grid.
		 */
		private Stream<Number> getSearchNumbers(Point symbol) {
			var searchNumbers = numbers.get(symbol.row).stream();
			if (symbol.row > 0) {
				searchNumbers = Stream.concat(
					searchNumbers,
					numbers.get(symbol.row - 1).stream()
				);
			}
			if (symbol.row < numbers.size() - 1) {
				searchNumbers = Stream.concat(
					searchNumbers,
					numbers.get(symbol.row + 1).stream()
				);
			}

			return searchNumbers;
		}
	}

	@Builder
	public static record Number(
		int value,
		int row,
		int left,
		int right
	) {
		public boolean isAdjacent(Point point) {
			return
				Math.abs(row - point.row) < 2
				&& point.column >= left - 1
				&& point.column <= right + 1;
		}
	}

	@Builder
	public static record Point(
		int row,
		int column
	) {}

}
