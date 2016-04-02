package com.github.marschall.threeten.spliterators;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Factory methods for streams over ranges of {@link Temporal}s
 * with an arbitrary increment.
 *
 * @implNote
 * The {@link Spliterator}s backing the streams will not support splitting.
 */
public final class TemporalStreams {

  private TemporalStreams() {
    throw new AssertionError("not instantiable");
  }

  /**
   * Returns a sequential ordered {@code Stream<Temporal>} from
   * {@code startInclusive} (inclusive) to {@code endInclusive}
   * (exclusive) by an incremental given by {@code adjuster}.
   *
   * <p>If {@code startInclusive} is bigger than {@code endInclusive}
   * a decremental step given by {@code adjuster} is performed instead.</p>
   *
   * @param startInclusive the (inclusive) initial value
   * @param endExclusive the exclusive bound
   * @return a sequential {@code Stream} for the range of {@code Temporal}
   *         elements
   */
  public static <T extends Temporal & Comparable<?>> Stream<T> range(T startInclusive, T endExclusive, TemporalAdjuster adjuster) {
    if (((Comparable) startInclusive).compareTo(endExclusive) <= 0) {
      return StreamSupport.stream(new AscendingExclusiveTemporalSpliterator<>(startInclusive, endExclusive, adjuster), false);
    } else {
      return StreamSupport.stream(new DescendingExclusiveTemporalSpliterator<>(startInclusive, endExclusive, adjuster), false);
    }
  }

  /**
   * Returns a sequential ordered {@code Stream<Temporal>} from
   * {@code startInclusive} (inclusive) to {@code endInclusive}
   * (inclusive) by an incremental given by {@code adjuster}.
   *
   * <p>If {@code startInclusive} is bigger than {@code endInclusive}
   * a decremental step given by {@code adjuster} is performed instead.</p>
   *
   * @param startInclusive the (inclusive) initial value
   * @param endInclusive the inclusive bound
   * @return a sequential {@code Stream} for the range of {@code Temporal}
   *         elements
   */
  public static <T extends Temporal & Comparable<?>> Stream<T> rangeClosed(T startInclusive, T endInclusive, TemporalAdjuster adjuster) {
    if (((Comparable) startInclusive).compareTo(endInclusive) <= 0) {
      return StreamSupport.stream(new AscendingInclusiveTemporalSpliterator<>(startInclusive, endInclusive, adjuster), false);
    } else {
      return StreamSupport.stream(new DescendingInclusiveTemporalSpliterator<>(startInclusive, endInclusive, adjuster), false);
    }
  }

  static abstract class TemporalSpliterator<T extends Temporal & Comparable<?>> implements Spliterator<T> {

    /**
     * Position of the next read.
     */
    private T current;
    private final T last;
    private final TemporalAdjuster adjuster;

    TemporalSpliterator(T current, T last, TemporalAdjuster adjuster) {
      Objects.requireNonNull(current, "startInclusive");
      Objects.requireNonNull(last, "endInclusive");
      Objects.requireNonNull(adjuster, "adjuster");
      this.current = current;
      this.last = last;
      this.adjuster = adjuster;
    }


    @Override
    public void forEachRemaining(Consumer<? super T> action) {
      while (!isAtEnd()) {
        action.accept(this.current);
        this.current = (T) this.current.with(adjuster);
      }
    }

    private boolean isAtEnd() {
      return this.isAtEnd(this.current, this.last);
    }

    abstract boolean isAtEnd(T current, T last);


    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
      if (isAtEnd()) {
        return false;
      }
      action.accept(this.current);
      this.current = (T) this.current.with(adjuster);
      return true;
    }

    @Override
    public Spliterator<T> trySplit() {
      return null;
    }

    @Override
    public long estimateSize() {
      return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
      return IMMUTABLE | NONNULL;
    }
  }

  static final class AscendingInclusiveTemporalSpliterator<T extends Temporal & Comparable<?>> extends TemporalSpliterator<T> {

    AscendingInclusiveTemporalSpliterator(T current, T last, TemporalAdjuster adjuster) {
      super(current, last, adjuster);
    }

    @Override
    boolean isAtEnd(T current, T last) {
      return ((Comparable) current).compareTo(last) > 0;
    }

  }

  static final class AscendingExclusiveTemporalSpliterator<T extends Temporal & Comparable<?>> extends TemporalSpliterator<T> {

    AscendingExclusiveTemporalSpliterator(T current, T last, TemporalAdjuster adjuster) {
      super(current, last, adjuster);
    }

    @Override
    boolean isAtEnd(T current, T last) {
      return ((Comparable) current).compareTo(last) >= 0;
    }

  }

  static final class DescendingInclusiveTemporalSpliterator<T extends Temporal & Comparable<?>> extends TemporalSpliterator<T> {

    DescendingInclusiveTemporalSpliterator(T current, T last, TemporalAdjuster adjuster) {
      super(current, last, adjuster);
    }

    @Override
    boolean isAtEnd(T current, T last) {
      return ((Comparable) current).compareTo(last) < 0;
    }

  }

  static final class DescendingExclusiveTemporalSpliterator<T extends Temporal & Comparable<?>> extends TemporalSpliterator<T> {

    DescendingExclusiveTemporalSpliterator(T current, T last, TemporalAdjuster adjuster) {
      super(current, last, adjuster);
    }

    @Override
    boolean isAtEnd(T current, T last) {
      return ((Comparable) current).compareTo(last) <= 0;
    }

  }

}
