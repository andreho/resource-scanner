package net.andreho.resources;

import net.andreho.resources.impl.FileResourceResolverImpl;
import net.andreho.resources.impl.JarResourceResolverImpl;
import net.andreho.resources.impl.ResourceResolverPipelineImpl;

import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Created by a.hofmann on 13.05.2016.
 */
@FunctionalInterface
public interface ResourceResolver {

  /**
   * Resolves given URL to all available resources using given resource filter
   *
   * @param url          of underlying resources (e.g.: a directory, *.jar, etc.)
   * @param filter       to use for resource filtering
   * @param typeSelector for resource's type selection
   * @return a map with available filtered resources
   */
  Optional<Map<String, Resource>> resolve(URL url,
                                          ResourceFilter filter,
                                          ResourceTypeSelector typeSelector);

  /**
   * Creates a pipeline for resource resolution using given resolution strategies
   *
   * @param resourceResolvers to use
   * @return a resource resolution strategy
   */
  static ResourceResolver with(ResourceResolver... resourceResolvers) {
    return new ResourceResolverPipelineImpl(resourceResolvers);
  }

  /**
   * Creates a pipeline for resource resolution using given resolution strategies
   *
   * @param resourceResolvers to use
   * @return a resource resolution strategy
   */
  static ResourceResolver with(Collection<ResourceResolver> resourceResolvers) {
    return new ResourceResolverPipelineImpl(resourceResolvers);
  }

  /**
   * @return
   */
  static ResourceResolver newFileResourceResolver() {
    return new FileResourceResolverImpl();
  }

  /**
   * @return
   */
  static ResourceResolver newJarResourceResolver() {
    return new JarResourceResolverImpl();
  }
}
