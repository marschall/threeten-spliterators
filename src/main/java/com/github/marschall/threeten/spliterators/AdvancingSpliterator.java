package com.github.marschall.threeten.spliterators;

import java.util.Spliterator;
import java.util.function.Consumer;;

abstract class AdvancingSpliterator<T> implements Spliterator<T> {

  private static final int CHARACTERISTIS = SIZED | SUBSIZED | IMMUTABLE | NONNULL | DISTINCT;

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
    return CHARACTERISTIS;
  }

}