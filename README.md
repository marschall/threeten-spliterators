JSR-310 Spliterators [![Build Status](https://travis-ci.org/marschall/threeten-spliterators.png?branch=master)](https://travis-ci.org/marschall/threeten-spliterators) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/threeten-spliterators/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.marschall/threeten-spliterators)
====================

Spliterators for JSR-310 (Java Date Time API) data types allowing you to create streams over JSR-310 data types.

For example you can iterate over all days in a year

```java
LocalDateStreams.range(LocalDate.of(2016, 1, 1), LocalDate.of(2016, 2, 1))
  .forEach((localDate) -> {
    // body
});
```

or over all months in a year

```java
YearMonthStreams.rangeClosed(YearMonth.of(2016, 1), YearMonth.of(2016, 12))
  .forEach((yearMonth) -> {
    // body
});
```

Besides the predefined iterations by day for `LocalDate` and month for `YearMonth` we offer a generic way to iterate over any `Temporal` with any increment given by a `TemporalAdjuster`.

For example you can iterate over all Mondays.

```java
LocalDate start = LocalDate.of(2016, 1, 1).with(nextOrSame(MONDAY));
LocalDate end = LocalDate.of(2016, 1, 1).with(lastDayOfMonth());
TemporalStreams.rangeClosed(start, end, next(MONDAY))
  .forEach((localDate) -> {
    // body
});
```


All the stream methods like `#map` or `#filter` are available.

For more information check out the [Javadoc](http://www.javadoc.io/doc/com.github.marschall/threeten-spliterators).
