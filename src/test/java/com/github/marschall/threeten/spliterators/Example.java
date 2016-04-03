package com.github.marschall.threeten.spliterators;

import static java.time.DayOfWeek.MONDAY;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.nextOrSame;

import java.time.LocalDate;
import java.time.YearMonth;

public class Example {

  public void iterateOverDaysInMonth() {
    LocalDateStreams.range(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 2, 1))
      .forEach((localDate) -> {
        // body
    });
  }

  public void iterateOverMonthsInYear() {
    YearMonthStreams.rangeClosed(YearMonth.of(2016, 1), YearMonth.of(2016, 12))
      .forEach((yearMonth) -> {
        // body
    });
  }

  public void iterateOverMondaysInYear() {
    LocalDate start = LocalDate.of(2016, 1, 1).with(nextOrSame(MONDAY));
    LocalDate end = LocalDate.of(2016, 1, 1).with(lastDayOfMonth());
    TemporalStreams.rangeClosed(start, end, next(MONDAY))
      .forEach((localDate) -> {
        // body
    });
  }

}
