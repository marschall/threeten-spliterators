package com.github.marschall.threeten.spliterators;

import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.YearMonth;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class YearMonthStream {

  private YearMonthStream() {
    throw new AssertionError("not instantiable");
  }

  /**
   * Returns a sequential ordered {@code Stream<YearMonth>} from
   * {@code startInclusive} (inclusive) to {@code endExclusive}
   * (exclusive) by an incremental step of one month.
   *
   * <p>If {@code startInclusive} is bigger than {@code endExclusive}
   * a decremental step of one month is performed instead.</p>
   *
   * @param startInclusive the (inclusive) initial value
   * @param endExclusive the exclusive bound
   * @return a sequential {@code Stream} for the range of {@code YearMonth}
   *         elements
   */
  public static Stream<YearMonth> range(YearMonth startInclusive, YearMonth endExclusive) {
    long monthsBetween = MONTHS.between(startInclusive, endExclusive);
    if (monthsBetween == 0L) {
      return Stream.empty();
    }
    if (monthsBetween > 0L) {
      return StreamSupport.stream(new IncrementingYearMonthSpliterator(startInclusive, monthsBetween), false);
    } else {
      return StreamSupport.stream(new DecrementingYearMonthSpliterator(startInclusive, -monthsBetween), false);
    }
  }

  /**
   * Returns a sequential ordered {@code Stream<YearMonth>} from
   * {@code startInclusive} (inclusive) to {@code endInclusive}
   * (inclusive) by an incremental step of one month.
   *
   * <p>If {@code startInclusive} is bigger than {@code endInclusive}
   * a decremental step of one month is performed instead.</p>
   *
   * @param startInclusive the (inclusive) initial value
   * @param endInclusive the inclusive bound
   * @return a sequential {@code Stream} for the range of {@code YearMonth}
   *         elements
   */
  public static Stream<YearMonth> rangeClosed(YearMonth startInclusive, YearMonth endInclusive) {
    long monthsBetween = MONTHS.between(startInclusive, endInclusive);
    if (monthsBetween >= 0L) {
      return StreamSupport.stream(new IncrementingYearMonthSpliterator(startInclusive, monthsBetween + 1L), false);
    } else {
      return StreamSupport.stream(new DecrementingYearMonthSpliterator(startInclusive, -monthsBetween + 1L), false);
    }
  }

  static abstract class AdvancingSpliterator<T> implements Spliterator<T> {

    /**
     * Position of the next read.
     */
    private T current;
    private long left;


    AdvancingSpliterator(T current, long left) {
      this.current = current;
      this.left = left;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
      while (this.left > 0) {
        action.accept(this.current);
        this.current = advance();
        this.left -= 1;
      }
    }

    private T advance(long count) {
      return this.advance(this.current, count);
    }

    private T advance() {
      return this.advance(this.current, 1L);
    }

    abstract T advance(T current, long count);

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
      if (this.left == 0) {
        return false;
      }
      action.accept(this.current);
      this.current = advance();
      this.left -= 1;
      return true;
    }

    @Override
    public Spliterator<T> trySplit() {
      if (this.left < 1) {
        // empty or size 1 => null
        return null;
      }
      long half = this.left / 2L;
      this.left -= half;
      return this.newInstance(this.advance(this.left), half);
    }

    abstract Spliterator<T> newInstance(T current, long left);

    @Override
    public long estimateSize() {
      return this.left;
    }

    public long getExactSizeIfKnown() {
      return estimateSize();
    }

    @Override
    public int characteristics() {
      return Spliterator.SUBSIZED;
    }

  }

  abstract static class YearMonthSpliterator extends AdvancingSpliterator<YearMonth> {

    // move the bridge methods here

    YearMonthSpliterator(YearMonth current, long left) {
      super(current, left);
    }

  }

  static final class IncrementingYearMonthSpliterator extends YearMonthSpliterator {

    IncrementingYearMonthSpliterator(YearMonth current, long left) {
      super(current, left);
    }

    @Override
    Spliterator<YearMonth> newInstance(YearMonth current, long left) {
      return new IncrementingYearMonthSpliterator(current, left);
    }

    @Override
    protected YearMonth advance(YearMonth current, long count) {
      return current.plusMonths(count);
    }

  }

  static final class DecrementingYearMonthSpliterator extends YearMonthSpliterator {

    DecrementingYearMonthSpliterator(YearMonth current, long left) {
      super(current, left);
    }

    @Override
    Spliterator<YearMonth> newInstance(YearMonth current, long left) {
      return new DecrementingYearMonthSpliterator(current, left);
    }

    @Override
    protected YearMonth advance(YearMonth current, long count) {
      return current.minusMonths(count);
    }

  }

}
