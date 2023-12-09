package advent2023.days;

import static java.util.Map.entry;

import java.io.BufferedReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.NonNull;

public class Day_07 {

	public static Integer part_01(@NonNull BufferedReader input) {
		final var handsAscending = input.lines().map(Day_07::parseHandWithNoWilds).sorted().toList();

		int winnings = 0;
		for (var i = 0; i < handsAscending.size(); i++) {
			winnings += (i + 1) * handsAscending.get(i).bid;
		}
		return winnings;
	}

	public static Integer part_02(@NonNull BufferedReader input) {
		final var handsAscending = input.lines().map(Day_07::parseHandWithWilds).sorted().toList();

		int winnings = 0;
		for (var i = 0; i < handsAscending.size(); i++) {
			winnings += (i + 1) * handsAscending.get(i).bid;
		}
		return winnings;
	}

	private static Hand parseHandWithNoWilds(String line) {
		final var components = line.split("\\s+");
		return Hand.withNoWildCards(components[0], Integer.valueOf(components[1]));
	}

	private static Hand parseHandWithWilds(String line) {
		final var components = line.split("\\s+");
		return Hand.withWildCards(components[0], Integer.valueOf(components[1]));
	}

	@Builder
	public static record Hand (
		String contents,
		int bid,
		Type type,
		JacksWild jacksWild
	) implements Comparable<Hand> {

		public static Hand withNoWildCards(@NonNull String contents, int bid) {
			assert contents.length() == 5;
			return Hand.builder()
				.contents(contents)
				.bid(bid)
				.type(calcHandType(contents, JacksWild.NO))
				.jacksWild(JacksWild.NO)
				.build();
		}

		public static Hand withWildCards(@NonNull String contents, int bid) {
			assert contents.length() == 5;
			return Hand.builder()
				.contents(contents)
				.bid(bid)
				.type(calcHandType(contents, JacksWild.YES))
				.jacksWild(JacksWild.YES)
				.build();
		}

		private static Type calcHandType(@NonNull String contents, JacksWild wild) {
			final var countsByLabel = contents.chars()
				.mapToObj(val -> (char)val)
				.collect(Collectors.groupingBy(Function.identity()));
			var countStream = countsByLabel.entrySet().stream();

			if (wild == JacksWild.YES) { // don't consider jacks for primary sorting if they're wild
				countStream = countStream.filter(entry -> entry.getKey() != 'J');
			}
			final var countsDescending =
				countStream
				.map(entry -> entry.getValue().size())
				.sorted(Comparator.reverseOrder())
				.toList();

			final var wildCount = switch(wild) {
				case YES: yield countsByLabel.getOrDefault('J', Collections.emptyList()).size();
				case NO: yield 0;
			};

			// this special case is a pain so we just treat it super basically
			if (wild == JacksWild.YES && wildCount == 5) {
				return Type.FIVE_KIND;
			}

			final var biggestGroup = countsDescending.get(0) + wildCount;
			final var secondBiggest = countsDescending.size() > 1 ? countsDescending.get(1) : 0;
			return switch(biggestGroup) {
				case 5: yield Type.FIVE_KIND;
				case 4: yield Type.FOUR_KIND;
				case 3: yield secondBiggest == 2 ? Type.FULL_HOUSE : Type.THREE_KIND;
				case 2: yield secondBiggest == 2 ? Type.TWO_PAIR : Type.ONE_PAIR;
				default: yield Type.HIGH_CARD;
			};
		}

		@Override
		public int compareTo(Hand o) {
			if (this.type.rank != o.type.rank) {
				return this.type.rank - o.type.rank;
			}

			var compareRanks = 0;
			for (var i = 0; i < 5 && compareRanks == 0; i++) {
				compareRanks =
					getCardRank(this.contents.charAt(i), this.jacksWild)
					- getCardRank(o.contents.charAt(i), this.jacksWild);
			}
			return compareRanks;
		}

		public static enum Type {
			FIVE_KIND(7),
			FOUR_KIND(6),
			FULL_HOUSE(5),
			THREE_KIND(4),
			TWO_PAIR(3),
			ONE_PAIR(2),
			HIGH_CARD(1),
			;

			public final int rank;

			private Type(int rank) {
				this.rank = rank;
			}
		}

	}

	public static enum JacksWild {
		YES,
		NO,
	};

	public static int getCardRank(char c, JacksWild wild) {
		if (c == 'J') {
			return switch (wild) {
				case YES: yield 1;
				case NO: yield 11;
			};
		} else {
			return cardRanks.get(c);
		}
	}

	public static Map<Character, Integer> cardRanks = Map.ofEntries(
		entry('A', 14),
		entry('K', 13),
		entry('Q', 12),
		// non-wild J goes here
		entry('T', 10),
		entry('9', 9),
		entry('8', 8),
		entry('7', 7),
		entry('6', 6),
		entry('5', 5),
		entry('4', 4),
		entry('3', 3),
		entry('2', 2)
		// wild J goes here
	);

}

