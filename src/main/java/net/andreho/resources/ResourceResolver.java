package net.andreho.resources;

import net.andreho.resources.impl.FileResourceResolverImpl;
import net.andreho.resources.impl.JarResourceResolverImpl;
import net.andreho.resources.impl.ResourceResolverPipelineImpl;

import java.net.URL;
import java.util.Collection;
import java.util.Map;

/**
 * Created by a.hofmann on 13.05.2016.
 */
@FunctionalInterface
public interface ResourceResolver {

  /**
   * Creates a pipeline for resource resolution using given resolution strategies
   *
   * @param resourceResolvers to use
   * @return a resource resolution strategy
   */
  static ResourceResolver resolversList(ResourceResolver... resourceResolvers) {
    return new ResourceResolverPipelineImpl(resourceResolvers);
  }

  /**
   * Creates a pipeline for resource resolution using given resolution strategies
   *
   * @param resourceResolvers to use
   * @return a resource resolution strategy
   */
  static ResourceResolver resolversList(Collection<ResourceResolver> resourceResolvers) {
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

  /**
   * Resolves given URL to all available resources using given resource filter
   *
   * @param url          of underlying resources (e.g.: a directory, *.jar, etc.)
   * @param filter       to use for resource filtering
   * @param typeSelector for resource's type selection
   * @return a map with available filtered resources
   */
  Map<String, Resource> resolve(URL url,
                                ResourceFilter filter,
                                ResourceTypeSelector typeSelector);
}
