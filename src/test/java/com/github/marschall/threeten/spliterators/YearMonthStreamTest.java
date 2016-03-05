package com.github.marschall.threeten.spliterators;

import static org.junit.Assert.assertEquals;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class YearMonthStreamTest {

  @Test
  public void emptyRange() {
    Stream<YearMonth> stream = YearMonthStream.range(YearMonth.of(2016, 1), YearMonth.of(2016, 1));
    assertEquals(0L, stream.count());
  }

  @Test
  public void range() {
    Stream<YearMonth> stream = YearMonthStream.range(YearMonth.of(2016, 1), YearMonth.of(2016, 3));
    List<YearMonth> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(YearMonth.of(2016, 1), YearMonth.of(2016, 2)), actual);
  }

  @Test
  public void rangeClosed() {
    Stream<YearMonth> stream = YearMonthStream.rangeClosed(YearMonth.of(2016, 1), YearMonth.of(2016, 2));
    List<YearMonth> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(YearMonth.of(2016, 1), YearMonth.of(2016, 2)), actual);
  }

}
