package net.andreho.resources.abstr;

import net.andreho.resources.ResourceType;

import java.util.Objects;

/**
 * Created by a.hofmann on 09.05.2016.
 */
public abstract class AbstractResourceType implements ResourceType {

  private final String name;

  public AbstractResourceType(final String name) {
    this.name = Objects.requireNonNull(name, "name");
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ResourceType)) {
      return false;
    }
    ResourceType other = (ResourceType) o;
    return Objects.equals(getName(), other.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getName());
  }

  @Override
  public String toString() {
    return this.getName();
  }
}
