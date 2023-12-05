package advent2023.days;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.NonNull;

public class Day_04 {

	private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

	public static Integer part_01(@NonNull BufferedReader input) {
		return input.lines()
			.map(ScratchPlay::parse)
			.map(play -> play.getScore())
			.reduce(Integer::sum)
			.get();
	}

	public static Integer part_02(@NonNull BufferedReader input) {
		final var cards = input.lines().map(ScratchPlay::parse).toList();

		final int[] copies = new int[cards.size()];
		Arrays.fill(copies, 1);

		for (var i = 0; i < copies.length; i++) {
			final var card = cards.get(i);
			final var cardCopies = copies[i];
			final var score = card.getMatchedNumbers();

			for (
				var j = i + 1;
				j <= i + score && j < copies.length;
				j++
			) {
				copies[j] += cardCopies;
			}
		}

		return Arrays.stream(copies).reduce(Integer::sum).getAsInt();
	}

	@Builder
	public static record ScratchPlay (
		int cardNumber,
		@NonNull Set<Integer> winners,
		@NonNull Set<Integer> myNumbers
	) {
		// as usual, assume valid syntax in our input
		public static ScratchPlay parse(String input) {
			final var cardAndNumbers = input.split("\\:");

			final var numberMatcher = NUMBER_PATTERN.matcher(cardAndNumbers[0]);
			numberMatcher.find();
			final var cardNumber = Integer.valueOf(numberMatcher.group());

			final var winnersAndMyNumbers = cardAndNumbers[1].split("\\|");
			return ScratchPlay.builder()
				.cardNumber(cardNumber)
				.winners(
					numberMatcher.reset(winnersAndMyNumbers[0])
						.results()
						.map(MatchResult::group)
						.map(Integer::valueOf)
						.collect(Collectors.toSet())
				).myNumbers(
					numberMatcher.reset(winnersAndMyNumbers[1])
						.results()
						.map(MatchResult::group)
						.map(Integer::valueOf)
						.collect(Collectors.toSet())
				).build();
		}

		public int getMatchedNumbers() {
			return (int) myNumbers.stream().filter(myNum -> winners.contains(myNum)).count();
		}

		public int getScore() {
			final var matches = myNumbers.stream().filter(myNum -> winners.contains(myNum)).count();
			return switch (getMatchedNumbers()) {
				case 0: yield 0;
				default: yield 1 << (matches - 1);
			};
		}
	}

}
