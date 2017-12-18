package net.andreho.resources.impl;

import net.andreho.resources.ResourceType;
import net.andreho.resources.ResourceTypeSelector;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Created by a.hofmann on 13.05.2016.
 */
public class ResourceTypeSelectorImpl
    implements ResourceTypeSelector {

  private final Collection<ResourceType> resourceTypes;

  public ResourceTypeSelectorImpl(final ResourceType... resourceTypes) {
    this(Arrays.asList(resourceTypes));
  }

  public ResourceTypeSelectorImpl(final Collection<ResourceType> resourceTypes) {
    this.resourceTypes = Collections.unmodifiableCollection(Objects.requireNonNull(resourceTypes, "resourceTypes"));
  }

  @Override
  public Collection<ResourceType> getResourceTypes() {
    return resourceTypes;
  }

  @Override
  public ResourceType select(final String resourceName) {
    for (ResourceType resourceType : getResourceTypes()) {
      if (resourceType.isCompatibleWith(resourceName)) {
        return resourceType;
      }
    }
    return ResourceType.UNKNOWN_TYPE;
  }
}
