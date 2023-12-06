package advent2023.days;

import static advent2023.Util.NUMBER_PATTERN;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.NonNull;

public class Day_04 {

	public static Integer part_01(@NonNull BufferedReader input) {
		return input.lines()
			.map(ScratchPlay::parse)
			.map(play -> play.score)
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
			final var matches = card.matches;

			for (
				var j = i + 1;
				j <= i + matches && j < copies.length;
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
		@NonNull Set<Integer> myNumbers,
		int matches,
		int score
	) {
		// as usual, assume valid syntax in our input
		public static ScratchPlay parse(String input) {
			final var cardAndNumbers = input.split("\\:");

			final var numberMatcher = NUMBER_PATTERN.matcher(cardAndNumbers[0]);
			numberMatcher.find();
			final var cardNumber = Integer.valueOf(numberMatcher.group());

			final var winnersAndMyNumbers = cardAndNumbers[1].split("\\|");
			final var winners = numberMatcher.reset(winnersAndMyNumbers[0])
				.results()
				.map(MatchResult::group)
				.map(Integer::valueOf)
				.collect(Collectors.toSet());
			final var myNumbers = numberMatcher.reset(winnersAndMyNumbers[1])
				.results()
				.map(MatchResult::group)
				.map(Integer::valueOf)
				.collect(Collectors.toSet());
			final var matches =
				(int) myNumbers.stream()
					.filter(myNum -> winners.contains(myNum))
					.count();
			final var score = switch (matches) {
				case 0: yield 0;
				default: yield 1 << (matches - 1);
			};

			return ScratchPlay.builder()
				.cardNumber(cardNumber)
				.winners(winners)
				.myNumbers(myNumbers)
				.matches(matches)
				.score(score)
				.build();
		}
	}

}
