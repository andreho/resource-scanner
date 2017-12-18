package net.andreho.resources.impl;

import net.andreho.resources.abstr.AbstractResourceType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by a.hofmann on 09.05.2016.
 */
public class PatternResourceTypeImpl
    extends AbstractResourceType {

  private final Pattern[] patterns;

  public PatternResourceTypeImpl(String name,
                                 String... patterns) {
    super(name);
    this.patterns = new Pattern[patterns.length];
    for (int i = 0; i < patterns.length; i++) {
      this.patterns[i] = Pattern.compile(patterns[i]);
    }
  }

  @Override
  public boolean isCompatibleWith(final String resourceName) {
    for (Pattern pattern : this.patterns) {
      final Matcher matcher = pattern.matcher(resourceName);
      if (matcher.matches()) {
        return true;
      }
    }
    return false;
  }
}
