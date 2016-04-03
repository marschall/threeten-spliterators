package com.github.marschall.threeten.spliterators;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class TemporalStreamsTest {

  @Test
  public void empty() {
    Stream<LocalDate> stream = TemporalStreams.range(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 1), TemporalAdjusters.next(DayOfWeek.MONDAY));
    assertEquals(0, stream.count());
  }

  @Test
  public void emptyClosed() {
    Stream<LocalDate> stream = TemporalStreams.rangeClosed(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 1), TemporalAdjusters.next(DayOfWeek.MONDAY));
    List<LocalDate> actual = stream.collect(Collectors.toList());
    assertEquals(Collections.singletonList(LocalDate.of(2016, 1, 1)), actual);
  }

  @Test
  public void rangeClosed() {
    Stream<LocalDate> stream = TemporalStreams.rangeClosed(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31), TemporalAdjusters.next(DayOfWeek.MONDAY));
    List<LocalDate> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 4), LocalDate.of(2016, 1, 11), LocalDate.of(2016, 1, 18), LocalDate.of(2016, 1, 25)), actual);

    stream = TemporalStreams.rangeClosed(LocalDate.of(2016, 1, 4), LocalDate.of(2016, 1, 25), TemporalAdjusters.next(DayOfWeek.MONDAY));
    actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 4), LocalDate.of(2016, 1, 11), LocalDate.of(2016, 1, 18), LocalDate.of(2016, 1, 25)), actual);
  }

  @Test
  public void range() {
    Stream<LocalDate> stream = TemporalStreams.range(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31), TemporalAdjusters.next(DayOfWeek.MONDAY));
    List<LocalDate> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 4), LocalDate.of(2016, 1, 11), LocalDate.of(2016, 1, 18), LocalDate.of(2016, 1, 25)), actual);

    stream = TemporalStreams.range(LocalDate.of(2016, 1, 4), LocalDate.of(2016, 1, 25), TemporalAdjusters.next(DayOfWeek.MONDAY));
    actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 4), LocalDate.of(2016, 1, 11), LocalDate.of(2016, 1, 18)), actual);
  }

  @Test
  public void reverseRangeClosed() {
    Stream<LocalDate> stream = TemporalStreams.rangeClosed(LocalDate.of(2016, 1, 31), LocalDate.of(2016, 1, 1), TemporalAdjusters.previous(DayOfWeek.MONDAY));
    List<LocalDate> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 31), LocalDate.of(2016, 1, 25), LocalDate.of(2016, 1, 18), LocalDate.of(2016, 1, 11), LocalDate.of(2016, 1, 4)), actual);

    stream = TemporalStreams.rangeClosed(LocalDate.of(2016, 1, 25), LocalDate.of(2016, 1, 4), TemporalAdjusters.previous(DayOfWeek.MONDAY));
    actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 25), LocalDate.of(2016, 1, 18), LocalDate.of(2016, 1, 11), LocalDate.of(2016, 1, 4)), actual);
  }

  @Test
  public void reverseRange() {
    Stream<LocalDate> stream = TemporalStreams.range(LocalDate.of(2016, 1, 31), LocalDate.of(2016, 1, 1), TemporalAdjusters.previous(DayOfWeek.MONDAY));
    List<LocalDate> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 31), LocalDate.of(2016, 1, 25), LocalDate.of(2016, 1, 18), LocalDate.of(2016, 1, 11), LocalDate.of(2016, 1, 4)), actual);

    stream = TemporalStreams.range(LocalDate.of(2016, 1, 25), LocalDate.of(2016, 1, 4), TemporalAdjusters.previous(DayOfWeek.MONDAY));
    actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 25), LocalDate.of(2016, 1, 18), LocalDate.of(2016, 1, 11)), actual);
  }

}
