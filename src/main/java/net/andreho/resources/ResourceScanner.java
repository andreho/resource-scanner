package net.andreho.resources;

import net.andreho.resources.impl.ResourceScannerImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by a.hofmann on 09.05.2016.
 */
public interface ResourceScanner {
  enum Parallelism {
    SINGLE,
    CONCURRENT
  }

  /**
   * @return whether this resource scanner is concurrent or not
   */
  Parallelism getParallelism();

  /**
   * @return locator of possible resource sources
   */
  ResourceSourceLocator getResourceSourceLocator();

  /**
   * @return associated strategy-pipeline for resource resolution
   */
  ResourceResolver getResourceResolver();

  /**
   * @return associated strategy for a selection of suitable resource types
   */
  ResourceTypeSelector getResourceTypeSelector();

  /**
   * @return associated resource's name filter
   */
  ResourceFilter getResourceFilter();

  /**
   * @param classLoader to scan
   */
  Map<String, Resource> scan(ClassLoader classLoader)
  throws IOException, URISyntaxException, ExecutionException, InterruptedException;

  /**
   * Creates default resource type implementation
   *
   * @param parallelism of scanner
   * @param sourceLocator to use
   * @param resolver      to use
   * @param filter        to use
   * @param types         to use
   * @return a resource scanner ready for use.
   */
  static ResourceScanner newScanner(Parallelism parallelism,
                                    ResourceSourceLocator sourceLocator,
                                    ResourceResolver resolver,
                                    ResourceFilter filter,
                                    ResourceType... types) {
    return new ResourceScannerImpl(parallelism, sourceLocator, resolver, filter, types);
  }

  /**
   * Creates default resource type implementation
   *
   * @param parallelism of scanner
   * @param sourceLocator to use
   * @param resolver      to use
   * @param filter        to use
   * @param typeSelector  to use
   * @return a resource scanner ready for use.
   */
  static ResourceScanner newScanner(Parallelism parallelism,
                                    ResourceSourceLocator sourceLocator,
                                    ResourceResolver resolver,
                                    ResourceFilter filter,
                                    ResourceTypeSelector typeSelector) {
    return new ResourceScannerImpl(parallelism, sourceLocator, resolver, filter, typeSelector);
  }
}
