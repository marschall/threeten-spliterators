package com.github.marschall.threeten.spliterators;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class TemporalStreamsTest {

  @Test
  public void rangeClosed() {
    Stream<LocalDate> stream = TemporalStreams.rangeClosed(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 31), TemporalAdjusters.next(DayOfWeek.MONDAY));
    List<LocalDate> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 4), LocalDate.of(2016, 1, 11), LocalDate.of(2016, 1, 18), LocalDate.of(2016, 1, 25)), actual);
  }

}
