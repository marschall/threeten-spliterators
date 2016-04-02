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
 */
final class TemporalStreams {

  private TemporalStreams() {
    throw new AssertionError("not instantiable");
  }

  /**
   * Returns a sequential ordered {@code Stream<Temporal>} from
   * {@code startInclusive} (inclusive) to {@code endInclusive}
   * (inclusive) by an incremental given by {@code adjuster}
   *
   * @param startInclusive the (inclusive) initial value
   * @param endInclusive the inclusive bound
   * @param adjuster the increment from one element to the next
   * @return a sequential {@code Stream} for the range of {@code Temporal}
   *         elements
   */
  static <T extends Temporal> Stream<T> rangeClosed(T startInclusive, T endInclusive, TemporalAdjuster adjuster) {
    return  StreamSupport.stream(new InclusiveTemporalSpliterator<>(startInclusive, endInclusive, adjuster), false);
  }

  static final class InclusiveTemporalSpliterator<T extends Temporal> implements Spliterator<T> {

    /**
     * Position of the next read.
     */
    private T current;
    private T last;
    private TemporalAdjuster adjuster;

    InclusiveTemporalSpliterator(T current, T last, TemporalAdjuster adjuster) {
      Objects.requireNonNull(current, "startInclusive");
      Objects.requireNonNull(last, "endInclusive");
      Objects.requireNonNull(adjuster, "adjuster");
      this.current = current;
      this.last = last;
      this.adjuster = adjuster;
    }


    @Override
    public void forEachRemaining(Consumer<? super T> action) {
      while (!this.current.equals(this.last)) {
        action.accept(this.current);
        this.current = (T) this.current.with(adjuster);
      }
    }


    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
      if (this.current.equals(this.last)) {
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
      return IMMUTABLE & NONNULL;
    }
  }

}
