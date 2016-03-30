JSR-310 Spliterators
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

All the stream methods like `#map` or `#filter` are available.

