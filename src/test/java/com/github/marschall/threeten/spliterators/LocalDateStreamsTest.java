package com.github.marschall.threeten.spliterators;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;


public class LocalDateStreamsTest {

  @Test
  public void emptyRange() {
    Stream<LocalDate> stream = LocalDateStreams.range(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 1));
    assertEquals(0L, stream.count());
  }

  @Test
  public void range() {
    Stream<LocalDate> stream = LocalDateStreams.range(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 3));
    List<LocalDate> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 2)), actual);
  }

  @Test
  public void rangeReversed() {
    Stream<LocalDate> stream = LocalDateStreams.range(LocalDate.of(2016, 1, 3), LocalDate.of(2016, 1, 1));
    List<LocalDate> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 3), LocalDate.of(2016, 1, 2)), actual);
  }

  @Test
  public void skip() {
    Stream<LocalDate> stream = LocalDateStreams.rangeClosed(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 12)).skip(10L);
    List<LocalDate> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 11), LocalDate.of(2016, 1, 12)), actual);
  }

  @Test
  public void rangeClosed() {
    Stream<LocalDate> stream = LocalDateStreams.rangeClosed(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 2));
    List<LocalDate> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 2)), actual);
  }

  @Test
  public void rangeClosedReverse() {
    Stream<LocalDate> stream = LocalDateStreams.rangeClosed(LocalDate.of(2016, 1, 2), LocalDate.of(2016, 1, 1));
    List<LocalDate> actual = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList(LocalDate.of(2016, 1, 2), LocalDate.of(2016, 1, 1)), actual);
  }

  @Test
  public void rangeClosedOne() {
    Stream<LocalDate> stream = LocalDateStreams.rangeClosed(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 1, 1));
    List<LocalDate> actual = stream.collect(Collectors.toList());
    assertEquals(Collections.singletonList(LocalDate.of(2016, 1, 1)), actual);
  }

}
