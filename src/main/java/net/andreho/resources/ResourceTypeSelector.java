package net.andreho.resources;

import net.andreho.resources.impl.ResourceTypeSelectorImpl;

import java.util.Collection;

/**
 * Created by a.hofmann on 13.05.2016.
 */
public interface ResourceTypeSelector {

  /**
   * @return known resource types
   */
  Collection<ResourceType> getResourceTypes();

  /**
   * Selects best suitable resource type from known types for the given resource name
   *
   * @param resourceName to analyze
   * @return a best suitable resource type
   * @see ResourceType
   */
  ResourceType select(String resourceName);

  /**
   * Creates a default type selector using given resource types
   *
   * @param resourceTypes to use
   * @return a best suitable resource type from given resource types
   */
  static ResourceTypeSelector with(ResourceType... resourceTypes) {
    return new ResourceTypeSelectorImpl(resourceTypes);
  }

  /**
   * Creates a default type selector using given resource types
   *
   * @param resourceTypes to use
   * @return a best suitable resource type from given resource types
   */
  static ResourceTypeSelector with(Collection<ResourceType> resourceTypes) {
    return new ResourceTypeSelectorImpl(resourceTypes);
  }
}
