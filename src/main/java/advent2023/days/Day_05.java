package advent2023.days;

import static advent2023.Util.NUMBER_PATTERN;
import static advent2023.Util.readLineFullSend;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.NonNull;

public class Day_05 {

	private static final Pattern MAP_PATTERN = Pattern.compile("map");

	public static Long part_01(@NonNull BufferedReader input) {
		final var seedNumbers = parseSeedNumbers(input);
		final var transformations = parseTransformations(input);

		return seedNumbers.stream()
			.map(number -> transformations.transformValue(number))
			.min(Comparator.naturalOrder())
			.get();
	}

	public static Long part_02(@NonNull BufferedReader input) {
		final var seedRanges = parseSeedRanges(input);
		final var transformations = parseTransformations(input);

		// calculate critical points of the mapping function
		final var testPoints = transformations.calculateBaseBoundaries();

		// filter our critical points to only those which appear in the input ranges, and then add
		// the endpoints of those input ranges
		final var testPointsInRange =
			Stream.concat(
				testPoints.stream().filter(point -> seedRanges.stream().anyMatch(range -> range.includes(point))),
				seedRanges.stream().flatMap(range -> Stream.of(range.rangeStart, range.rangeEnd))
			).collect(Collectors.toSet());

		return testPointsInRange.stream()
			.map(val -> transformations.transformValue(val))
			.min(Comparator.naturalOrder())
			.get();
	}

	private static List<Long> parseSeedNumbers(@NonNull BufferedReader input) {
		final var seedLine = readLineFullSend(input).split("\\:");
		return NUMBER_PATTERN.matcher(seedLine[1])
			.results()
			.map(MatchResult::group)
			.map(Long::valueOf)
			.toList();
	}

	private static List<Range> parseSeedRanges(@NonNull BufferedReader input) {
		final var seedLine = readLineFullSend(input).split("\\:");
		final var rawNumbers =
			NUMBER_PATTERN.matcher(seedLine[1])
			.results()
			.map(MatchResult::group)
			.map(Long::valueOf)
			.toList();

		assert rawNumbers.size() % 2 == 0;
		final var ranges = new ArrayList<Range>();
		for (var i = 0; i < rawNumbers.size(); i += 2) {
			final var baseNumber = rawNumbers.get(i);
			final var extentValue = rawNumbers.get(i + 1) + baseNumber - 1;
			ranges.add(Range.builder().rangeStart(baseNumber).rangeEnd(extentValue).build());
		}

		return ranges;
	}

	// as usual, assume well-formed input
	private static TransformStack parseTransformations(
		@NonNull BufferedReader input
	) {
		final var mapMatcher = MAP_PATTERN.matcher("");
		final List<List<AdditiveTransform>> mappings = new ArrayList<>();
		List<AdditiveTransform> mapSection = null;

		var line = readLineFullSend(input);
		while (line != null) {
			if (line.isBlank()) {
				line = readLineFullSend(input);
				continue;
			}

			mapMatcher.reset(line);
			if (mapMatcher.find()) { // if this is a line with "map" in it
				mapSection = new ArrayList<>();
				mappings.add(mapSection);
			} else { // not blank & not map means it's a numeric mapping line
				final var mapNumbers = line.split("\\s+");
				assert mapNumbers.length == 3; // sanity check

				final var rangeStart = Long.valueOf(mapNumbers[1]);
				// range is inclusive
				final var rangeEnd = rangeStart + Long.valueOf(mapNumbers[2]) - 1;
				final var transformTerm = Long.valueOf(mapNumbers[0]) - rangeStart;
				mapSection.add(
					AdditiveTransform.builder()
						.rangeStart(rangeStart)
						.rangeEnd(rangeEnd)
						.transformTerm(transformTerm)
						.build()
				);
			}

			line = readLineFullSend(input);
		}

		return new TransformStack(mappings);
	}

	public static class TransformStack {
		private final List<TransformLayer> layers;
		private final List<TransformLayer> inverseLayers;
		private final int layerCount;

		public TransformStack(List<List<AdditiveTransform>> rawTransforms) {
			layers = rawTransforms.stream().map(TransformLayer::new).toList();
			inverseLayers = layers.stream().map(TransformLayer::getInverse).toList();
			layerCount = layers.size();
		}

		public long transformValue(long value) {
			var output = value;
			for (var layer : layers) {
				output = layer.getTransformFor(output).perform(output);
			}
			return output;
		}

		private Set<Long> calculateBaseBoundaries() {
			Set<Long> points = new HashSet<>();
			for (var i = layerCount - 1; i > 0; i--) {
				final var currentLayer = i;
				points = Stream.concat(
					points.stream(),
					layers.get(currentLayer).getBoundaryPoints().stream()
				).map(point -> inverseLayers.get(currentLayer - 1).getTransformFor(point).perform(point))
				.collect(Collectors.toSet());
			}

			return Stream.concat(
				points.stream(),
				layers.get(0).getBoundaryPoints().stream()
			).collect(Collectors.toSet());
		}
	}

	public static class TransformLayer {

		private static final Comparator<LongSortable> SORT =
			Comparator.comparing(LongSortable::getKey);

		private final @NonNull List<AdditiveTransform> transforms;

		/**
		 * Constructs a TransformLayer object. This will sort given transforms, then fill in gaps
		 * with no-op transforms to contiguously cover the entire range from 0 to Long.MAX_VALUE.
		 */
		public TransformLayer(@NonNull List<AdditiveTransform> input) {
			// take a copy to make sure the list we have is mutable
			transforms = input.stream()
				.sorted(SORT) // verify they are sorted ascending
				.collect(Collectors.toCollection(ArrayList::new));

			// fill in any gaps so range fully covers 0 to max long value
			final var firstTransform = transforms.get(0);
			if (firstTransform.rangeStart > 0) {
				transforms.add(
					0,
					AdditiveTransform.builder()
						.rangeStart(0)
						.rangeEnd(firstTransform.rangeStart - 1)
						.transformTerm(0)
						.build()
				);
			}

			var i = 0;
			while (i < transforms.size() - 1) {
				final var current = transforms.get(i);
				final var next = transforms.get(i + 1);
				// fill in gap with a 0-transform if ranges are not contiguous
				if (next.rangeStart - current.rangeEnd > 1) {
					transforms.add(
						i + 1,
						AdditiveTransform.builder()
							.rangeStart(current.rangeEnd + 1)
							.rangeEnd(next.rangeStart - 1)
							.transformTerm(0)
							.build()
					);
				}
				i++;
			}

			final var lastTransform = transforms.get(transforms.size() - 1);
			if (lastTransform.rangeEnd < Long.MAX_VALUE) {
				transforms.add(
					transforms.size(),
					AdditiveTransform.builder()
						.rangeStart(lastTransform.rangeEnd + 1)
						.rangeEnd(Long.MAX_VALUE)
						.transformTerm(0)
						.build()
				);
			}
		}

		public AdditiveTransform getTransformFor(long value) {
			assert value >= 0;
			int index = Collections.binarySearch(transforms, new LongKey(value), SORT);
			if (index < 0) { // did not match a transform rangeStart exactly
				// since we're covering the entire valid range, we know this will never be 0, so
				// convert it back to the index PRIOR to its insertion point, which will be the
				// range containing the value
				index = -1 * (index + 1) - 1;
			}

			final var foundTransform = transforms.get(index);
			assert foundTransform.rangeStart <= value && value <= foundTransform.rangeEnd;

			return foundTransform;
		}

		public Set<Long> getBoundaryPoints() {
			return transforms.stream()
				.flatMap(transform -> Stream.of(
					transform.rangeStart - 1,
					transform.rangeStart,
					transform.rangeEnd,
					transform.rangeEnd + 1
				))
				.filter(val -> val >= 0)
				.collect(Collectors.toSet());
		}

		public TransformLayer getInverse() {
			return new TransformLayer(transforms.stream().map(AdditiveTransform::getInverse).toList());
		}
	}

	@Builder
	public static record Range (
		long rangeStart,
		long rangeEnd
	) {
		public boolean includes(long value) {
			return rangeStart <= value && value <= rangeEnd;
		}
	}

	public static interface LongSortable {
		long getKey();
	}

	@Builder
	public static record AdditiveTransform (
		long rangeStart,
		long rangeEnd, // inclusive
		long transformTerm
	) implements LongSortable {
		public long perform(long input) {
			if (input < rangeStart || input > rangeEnd) {
				throw new RuntimeException(String.format(
					"Attempted to transform value %d via invalid AdditiveTransform %s",
					input,
					this
				));
			}
			return transformTerm + input;
		}

		@Override
		public long getKey() {
			return rangeStart;
		}

		public AdditiveTransform getInverse() {
			return AdditiveTransform.builder()
				.rangeStart(rangeStart + transformTerm)
				.rangeEnd(rangeEnd + transformTerm)
				.transformTerm(-transformTerm)
				.build();
		}
	}

	public static record LongKey (
		long key
	) implements LongSortable {
		@Override
		public long getKey() {
			return key;
		}
	}

}
