package com.github.marschall.threeten.spliterators;

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

}
