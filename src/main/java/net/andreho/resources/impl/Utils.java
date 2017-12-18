package net.andreho.resources.impl;

import net.andreho.resources.Resource;

import java.util.Map;

/**
 * <br/>Created by a.hofmann on 16.06.2017 at 03:17.
 */
public abstract class Utils {

  private Utils() {
  }

  /**
   * Merges multiple entries by name into the result map
   *
   * @param result
   * @param other
   */
  public static void mergeResults(final Map<String, Resource> result,
                                  final Map<String, Resource> other) {
    for (final Map.Entry<String, Resource> entry : other.entrySet()) {
      final String key = entry.getKey();
      final Resource value = entry.getValue();

      result.merge(key, value, (prev, next) -> {
        next.setNext(prev);
        return next;
      });
    }
  }

  /**
   * @param str
   * @param suffix
   * @return
   */
  public static boolean endsWithCaseInsensitive(String str, String suffix) {
    if(str.length() < suffix.length()) {
      return false;
    }
    for(int j = 0, i = str.length() - suffix.length(), len = str.length(); i < len; j++, i++) {
      char a = str.charAt(i);
      char b = suffix.charAt(j);
      if(a != b) {
        if (Character.isLowerCase(a)) {
          if(a != Character.toLowerCase(b)) {
            return false;
          }
        } else {
          if(a != Character.toUpperCase(b)) {
            return false;
          }
        }
      }
    }
    return true;
  }
}
