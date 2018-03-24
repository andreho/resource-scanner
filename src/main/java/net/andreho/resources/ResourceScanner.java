package net.andreho.resources;

import net.andreho.resources.impl.ResourceScannerImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.SortedMap;
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
   * Scans defined resources using given classloader and other settings
   * @param classLoader to use for scanning
   * @return a map where keys are fully qualified resource names and values are their representatives
   */
  Map<String, Resource> scan(final ClassLoader classLoader)
  throws IOException, URISyntaxException, ExecutionException, InterruptedException;

  /**
   * Scans defined resources using given settings
   * @return a map where keys are fully qualified resource names and values are their representatives
   * @throws IOException
   * @throws URISyntaxException
   * @throws ExecutionException
   * @throws InterruptedException
   */
  default Map<String, Resource> scan()
  throws IOException, URISyntaxException, ExecutionException, InterruptedException {
    return scan(getClass().getClassLoader());
  }

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
  static ResourceScanner newResourceScanner(final Parallelism parallelism,
                                            final ResourceSourceLocator sourceLocator,
                                            final ResourceResolver resolver,
                                            final ResourceFilter filter,
                                            final ResourceType... types) {
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
  static ResourceScanner newResourceScanner(final Parallelism parallelism,
                                            final ResourceSourceLocator sourceLocator,
                                            final ResourceResolver resolver,
                                            final ResourceFilter filter,
                                            final ResourceTypeSelector typeSelector) {
    return new ResourceScannerImpl(parallelism, sourceLocator, resolver, filter, typeSelector);
  }
}
