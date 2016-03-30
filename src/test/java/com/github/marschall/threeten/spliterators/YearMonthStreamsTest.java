package com.github.marschall.threeten.spliterators;

import static org.junit.Assert.assertEquals;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class YearMonthStreamsTest {

  @Test
  public void emptyRange() {
    Stream<YearMonth> stream = YearMonthStreams.range(YearMonth.of(2016, 1), YearMonth.of(2016, 1));
    assertEquals(0L, stream.count());
  }

  @Test
  public void range() {
    Stream<YearMonth> stream = YearMonthStreams.range(YearMonth.of(2016, 1), YearMonth.of(2016, 3));
    List<YearMonth> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(YearMonth.of(2016, 1), YearMonth.of(2016, 2)), actual);
  }

  @Test
  public void rangeReversed() {
    Stream<YearMonth> stream = YearMonthStreams.range(YearMonth.of(2016, 3), YearMonth.of(2016, 1));
    List<YearMonth> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(YearMonth.of(2016, 3), YearMonth.of(2016, 2)), actual);
  }

  @Test
  public void skip() {
    Stream<YearMonth> stream = YearMonthStreams.rangeClosed(YearMonth.of(2016, 1), YearMonth.of(2016, 12)).skip(10L);
    List<YearMonth> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(YearMonth.of(2016, 11), YearMonth.of(2016, 12)), actual);
  }

  @Test
  public void rangeClosed() {
    Stream<YearMonth> stream = YearMonthStreams.rangeClosed(YearMonth.of(2016, 1), YearMonth.of(2016, 2));
    List<YearMonth> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(YearMonth.of(2016, 1), YearMonth.of(2016, 2)), actual);
  }

  @Test
  public void rangeClosedReverse() {
    Stream<YearMonth> stream = YearMonthStreams.rangeClosed(YearMonth.of(2016, 2), YearMonth.of(2016, 1));
    List<YearMonth> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(YearMonth.of(2016, 2), YearMonth.of(2016, 1)), actual);
  }

  @Test
  public void rangeClosedOne() {
    Stream<YearMonth> stream = YearMonthStreams.rangeClosed(YearMonth.of(2016, 1), YearMonth.of(2016, 1));
    List<YearMonth> actual = stream.collect(Collectors.toList());
    assertEquals(Collections.singletonList(YearMonth.of(2016, 1)), actual);
  }

}
