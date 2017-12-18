package net.andreho.resources.impl;

import net.andreho.resources.abstr.AbstractResourceType;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Boolean.TRUE;

/**
 * Created by a.hofmann on 09.05.2016.
 */
public class DelegatingResourceTypeImpl
    extends AbstractResourceType {
  private final Function<String, Boolean> delegate;

  public DelegatingResourceTypeImpl(String name,
                                    Function<String, Boolean> delegate) {
    super(name);
    this.delegate = Objects.requireNonNull(delegate, "Given delegate is null.");
  }

  @Override
  public boolean isCompatibleWith(final String resourceName) {
    return TRUE.equals(delegate.apply(resourceName));
  }
}
