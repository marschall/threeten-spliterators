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

  static abstract class AbstractYearMonthSpliterator implements Spliterator<YearMonth> {

    /**
     * Position of the next read.
     */
    private YearMonth current;
    private long left;


    AbstractYearMonthSpliterator(YearMonth current, long left) {
      this.current = current;
      this.left = left;
    }

    @Override
    public void forEachRemaining(Consumer<? super YearMonth> action) {
      while (this.left > 0) {
        action.accept(this.current);
        this.current = advance();
        this.left -= 1;
      }
    }

    private YearMonth advance() {
      return this.advance(this.current);
    }

    abstract YearMonth advance(YearMonth current);

    @Override
    public boolean tryAdvance(Consumer<? super YearMonth> action) {
      if (this.left == 0) {
        return false;
      }
      action.accept(this.current);
      this.current = advance();
      this.left -= 1;
      return true;
    }

    @Override
    public Spliterator<YearMonth> trySplit() {
      if (this.left < 1) {
        // empty or size 1 => null
        return null;
      }
      long half = this.left / 2L;
      this.left -= half;
      Spliterator<YearMonth> result = new IncrementingYearMonthSpliterator(this.current.plusMonths(this.left), half);
      return result;
    }
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


  static final class IncrementingYearMonthSpliterator extends AbstractYearMonthSpliterator {

    IncrementingYearMonthSpliterator(YearMonth current, long left) {
      super(current, left);
    }

    @Override
    protected YearMonth advance(YearMonth current) {
      return current.plusMonths(1L);
    }

  }

  static final class DecrementingYearMonthSpliterator extends AbstractYearMonthSpliterator {

    DecrementingYearMonthSpliterator(YearMonth current, long left) {
      super(current, left);
    }

    @Override
    protected YearMonth advance(YearMonth current) {
      return current.minusMonths(1L);
    }

  }

}
