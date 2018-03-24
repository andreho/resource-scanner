package net.andreho.resources.impl;

import net.andreho.resources.Resource;
import net.andreho.resources.ResourceFilter;
import net.andreho.resources.ResourceResolver;
import net.andreho.resources.ResourceTypeSelector;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by a.hofmann on 13.05.2016.
 */
public class ResourceResolverPipelineImpl implements ResourceResolver {

  private final Collection<ResourceResolver> resourceResolvers;

  public ResourceResolverPipelineImpl(final ResourceResolver... resourceResolvers) {
    this(Arrays.asList(resourceResolvers));
  }

  public ResourceResolverPipelineImpl(final Collection<ResourceResolver> resourceResolvers) {
    this.resourceResolvers = new ArrayList<>(Objects.requireNonNull(resourceResolvers));
  }

  public Collection<ResourceResolver> getResourceResolvers() {
    return Collections.unmodifiableCollection(resourceResolvers);
  }

  @Override
  public Optional<Map<String, Resource>> resolve(final URL url,
                                                 final ResourceFilter nameFilter,
                                                 final ResourceTypeSelector selector) {
    for (ResourceResolver resourceResolver : this.resourceResolvers) {
      Optional<Map<String, Resource>> resourceOptional = resourceResolver.resolve(url, nameFilter, selector);

      if (resourceOptional.isPresent()) {
        return resourceOptional;
      }
    }
    return Optional.empty();
  }
}
