package net.andreho.resources.impl;

import net.andreho.resources.Resource;
import net.andreho.resources.ResourceFilter;
import net.andreho.resources.ResourceResolver;
import net.andreho.resources.ResourceScanner;
import net.andreho.resources.ResourceSourceLocator;
import net.andreho.resources.ResourceType;
import net.andreho.resources.ResourceTypeSelector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static net.andreho.resources.impl.Utils.mergeResults;

/**
 * Created by a.hofmann on 10.05.2016.
 */
public class ResourceScannerImpl
  implements ResourceScanner {

  private final Parallelism parallelism;
  private final ResourceFilter resourceFilter;
  private final ResourceSourceLocator resourceSourceLocator;
  private final ResourceTypeSelector resourceTypeSelector;
  private final ResourceResolver resourceResolver;

  public ResourceScannerImpl(final Parallelism parallelism,
                             final ResourceSourceLocator resourceSourceLocator,
                             final ResourceResolver resourceResolver,
                             final ResourceFilter resourceFilter,
                             final ResourceType... resourceTypes) {
    this(parallelism,
         resourceSourceLocator,
         resourceResolver,
         resourceFilter,
         ResourceTypeSelector.selectorsList(resourceTypes)
    );
  }

  public ResourceScannerImpl(final Parallelism parallelism,
                             final ResourceSourceLocator resourceSourceLocator,
                             final ResourceResolver resourceResolver,
                             final ResourceFilter resourceFilter,
                             final ResourceTypeSelector resourceTypeSelector) {

    this.parallelism = Objects.requireNonNull(parallelism, "Parallelism can't be null.");
    this.resourceSourceLocator = Objects.requireNonNull(resourceSourceLocator, "resourceSourceLocator");
    this.resourceResolver = Objects.requireNonNull(resourceResolver, "resourceResolver");
    this.resourceFilter = Objects.requireNonNull(resourceFilter, "resourceFilter");
    this.resourceTypeSelector = Objects.requireNonNull(resourceTypeSelector, "resourceTypeSelector");
  }

  @Override
  public Parallelism getParallelism() {
    return parallelism;
  }

  @Override
  public ResourceSourceLocator getResourceSourceLocator() {
    return resourceSourceLocator;
  }

  @Override
  public ResourceResolver getResourceResolver() {
    return resourceResolver;
  }

  @Override
  public ResourceFilter getResourceFilter() {
    return resourceFilter;
  }

  @Override
  public ResourceTypeSelector getResourceTypeSelector() {
    return resourceTypeSelector;
  }

  @Override
  public Map<String, Resource> scan(final ClassLoader classLoader)
  throws IOException, URISyntaxException, ExecutionException, InterruptedException {
    return scanResourceSources(getResourceSourceLocator().locateResources(classLoader));
  }

  protected Map<String, Resource> createResultMap() {
    return new TreeMap<>();
  }

  protected Map<String, Resource> scanResourceSources(final Collection<URL> resourceSources)
  throws URISyntaxException, IOException, ExecutionException, InterruptedException {
    final List<Future<Optional<Map<String, Resource>>>> pendingList = new ArrayList<>(resourceSources.size());

    for (URL url : resourceSources) {
      pendingList.add(scanResourceSource(url));
    }

    final Map<String, Resource> resultMap = createResultMap();

    for (Future<Optional<Map<String, Resource>>> future : pendingList) {
      final Optional<Map<String, Resource>> optional = future.get();
      optional.ifPresent((resourceMap -> mergeResults(resultMap, resourceMap)));
    }

    return resultMap;
  }

  protected Future<Optional<Map<String, Resource>>> scanResourceSource(final URL resourceUrl) {
    if (getParallelism() == Parallelism.CONCURRENT) {
      return supplyAsync(() ->
                           getResourceResolver()
                             .resolve(resourceUrl, getResourceFilter(), getResourceTypeSelector())
      );
    }

    Optional<Map<String, Resource>> resolved = getResourceResolver()
      .resolve(resourceUrl, getResourceFilter(), getResourceTypeSelector());
    return CompletableFuture.completedFuture(resolved);
  }
}
