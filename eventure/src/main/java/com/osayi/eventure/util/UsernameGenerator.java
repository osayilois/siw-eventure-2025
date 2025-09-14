package com.osayi.eventure.util;

import java.util.Locale;
import java.util.function.Function;

public class UsernameGenerator {
  public static String unique(String base, Function<String, Boolean> exists) {
    String slug = base == null ? "" : base
        .toLowerCase(Locale.ITALIAN)
        .replaceAll("[^a-z0-9]+","")
        .replaceAll("^_|_$","");
    if (slug.isBlank()) slug = "user";
    String candidate = slug;
    int i = 1;
    while (exists.apply(candidate)) {
      candidate = slug + i++;
    }
    return candidate;
  }
}
