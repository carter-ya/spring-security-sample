package com.example.security.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 数据抽取
 *
 * @author LiuKeFeng
 * @date 2017-08-23
 */
public class Extractor {

  /**
   * list to map
   */
  public static <I, V> Map<I, V> toMap(List<V> list, Function<V, I> func) {
    return list.stream()
        .collect(Collectors.toMap(func, value -> value));
  }

  /**
   * iterable to map
   */
  public static <I, V> Map<I, V> toMap(Iterable<V> iterable, Function<V, I> func) {
    return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toMap(func, value -> value));
  }

  /**
   * map list
   */
  public static <I, V> List<I> map(List<V> list, Function<V, I> func) {
    return list.stream().map(func).collect(Collectors.toList());
  }

  /**
   * group list
   */
  public static <I, V> Map<I, List<V>> groupingBy(List<V> list, Function<V, I> func) {
    return list.stream().collect(Collectors.groupingBy(func));
  }

  /**
   * sum
   */
  public static <V> long longSum(List<V> list, ToLongFunction<V> func) {
    return list.stream().mapToLong(func).sum();
  }
}