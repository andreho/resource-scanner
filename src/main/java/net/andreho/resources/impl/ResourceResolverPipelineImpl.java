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
  public Map<String, Resource> resolve(final URL url,
                                       final ResourceFilter nameFilter,
                                       final ResourceTypeSelector selector) {
    for (ResourceResolver resourceResolver : this.resourceResolvers) {
      return Collections.unmodifiableMap(resourceResolver.resolve(url, nameFilter, selector));
    }
    return Collections.emptyMap();
  }
}
