package com.github.marschall.threeten.spliterators;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Factory methods for streams over ranges of {@link LocalDate}s.
 */
public final class LocalDateStreams {

  private LocalDateStreams() {
    throw new AssertionError("not instantiable");
  }

  /**
   * Returns a sequential ordered {@code Stream<LocalDate>} from
   * {@code startInclusive} (inclusive) to {@code endExclusive}
   * (exclusive) by an incremental step of one month.
   *
   * <p>If {@code startInclusive} is bigger than {@code endExclusive}
   * a decremental step of one month is performed instead.</p>
   *
   * @param startInclusive the (inclusive) initial value
   * @param endExclusive the exclusive bound
   * @return a sequential {@code Stream} for the range of {@code LocalDate}
   *         elements
   */
  public static Stream<LocalDate> range(LocalDate startInclusive, LocalDate endExclusive) {
    long monthsBetween = DAYS.between(startInclusive, endExclusive);
    if (monthsBetween == 0L) {
      return Stream.empty();
    }
    if (monthsBetween > 0L) {
      return StreamSupport.stream(new IncrementingLocalDateSpliterator(startInclusive, monthsBetween), false);
    } else {
      return StreamSupport.stream(new DecrementingLocalDateSpliterator(startInclusive, -monthsBetween), false);
    }
  }

  /**
   * Returns a sequential ordered {@code Stream<LocalDate>} from
   * {@code startInclusive} (inclusive) to {@code endInclusive}
   * (inclusive) by an incremental step of one month.
   *
   * <p>If {@code startInclusive} is bigger than {@code endInclusive}
   * a decremental step of one month is performed instead.</p>
   *
   * @param startInclusive the (inclusive) initial value
   * @param endInclusive the inclusive bound
   * @return a sequential {@code Stream} for the range of {@code LocalDate}
   *         elements
   */
  public static Stream<LocalDate> rangeClosed(LocalDate startInclusive, LocalDate endInclusive) {
    long monthsBetween = DAYS.between(startInclusive, endInclusive);
    if (monthsBetween >= 0L) {
      return StreamSupport.stream(new IncrementingLocalDateSpliterator(startInclusive, monthsBetween + 1L), false);
    } else {
      return StreamSupport.stream(new DecrementingLocalDateSpliterator(startInclusive, -monthsBetween + 1L), false);
    }
  }

  abstract static class LocalDateSpliterator extends AdvancingSpliterator<LocalDate> {

    // move the bridge methods here

    LocalDateSpliterator(LocalDate current, long left) {
      super(current, left);
    }

  }

  static final class IncrementingLocalDateSpliterator extends LocalDateSpliterator {

    IncrementingLocalDateSpliterator(LocalDate current, long left) {
      super(current, left);
    }

    @Override
    Spliterator<LocalDate> newInstance(LocalDate current, long left) {
      return new IncrementingLocalDateSpliterator(current, left);
    }

    @Override
    protected LocalDate advance(LocalDate current, long count) {
      return current.plusDays(count);
    }

  }

  static final class DecrementingLocalDateSpliterator extends LocalDateSpliterator {

    DecrementingLocalDateSpliterator(LocalDate current, long left) {
      super(current, left);
    }

    @Override
    Spliterator<LocalDate> newInstance(LocalDate current, long left) {
      return new DecrementingLocalDateSpliterator(current, left);
    }

    @Override
    protected LocalDate advance(LocalDate current, long count) {
      return current.minusDays(count);
    }

  }

}
