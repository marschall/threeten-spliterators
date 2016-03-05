package com.github.marschall.threeten.spliterators;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.YearMonth;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class YearMonthStream {

  private YearMonthStream() {
    throw new AssertionError("not instantiable");
  }

  public static Stream<YearMonth> range(YearMonth startInclusive, YearMonth endExclusive) {
    long daysBetween = DAYS.between(startInclusive, endExclusive);
    if (daysBetween == 0) {
      return Stream.empty();
    }
    return StreamSupport.stream(new YearMonthSpliterator(startInclusive, daysBetween), false);
  }


  public static Stream<YearMonth> rangeClosed(YearMonth startInclusive, YearMonth endInclusive) {
    long daysBetween = DAYS.between(startInclusive, endInclusive);
    if (daysBetween == 0) {
      return Stream.empty();
    }
    return StreamSupport.stream(new YearMonthSpliterator(startInclusive, daysBetween + 1L), false);
  }

  static final class YearMonthSpliterator implements Spliterator<YearMonth> {

    /**
     * Position of the next read.
     */
    private YearMonth current;
    private long left;


    YearMonthSpliterator(YearMonth current, long left) {
      this.current = current;
      this.left = left;
    }

    @Override
    public void forEachRemaining(Consumer<? super YearMonth> action) {
      while (this.left > 0) {
        action.accept(this.current);
        this.current = this.current.plusMonths(1L);
        this.left -= 1;
      }
    }

    @Override
    public boolean tryAdvance(Consumer<? super YearMonth> action) {
      if (this.left == 0) {
        return false;
      }
      action.accept(this.current);
      this.current = this.current.plusMonths(1L);
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
      Spliterator<YearMonth> result = new YearMonthSpliterator(this.current.plusMonths(this.left), half);
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

}
