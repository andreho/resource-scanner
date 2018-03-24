package net.andreho.resources;

import net.andreho.resources.impl.ClassLoaderResourceLocatorImpl;
import net.andreho.resources.impl.ClassPathResourceLocatorImpl;
import net.andreho.resources.impl.FileResourceResolverImpl;
import net.andreho.resources.impl.JarResourceResolverImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Map;

import static net.andreho.resources.ResourceResolver.newJarResourceResolver;
import static net.andreho.resources.ResourceResolver.resolversList;
import static net.andreho.resources.ResourceScanner.Parallelism.CONCURRENT;
import static net.andreho.resources.ResourceSourceLocator.locatorsList;
import static net.andreho.resources.ResourceSourceLocator.usingClassPath;
import static net.andreho.resources.ResourceSourceLocator.usingGivenClassLoader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Created by a.hofmann on 20.05.2016.
 */
class ResourceScannerTest {

  private static final String RESOURCE_PREFIX = "java/";

  @Test
  @DisplayName("All 4560 resources of Hibernate must be located")
  void selectResourcesOfHibernateFramework()
  throws Exception {
    ResourceScanner resourceScanner = ResourceScanner.newResourceScanner(
      CONCURRENT,
      locatorsList(
        usingGivenClassLoader(),
        usingClassPath()
      ),
      newJarResourceResolver(),
      (name, supplier) -> name.startsWith("org/hibernate/"),
      ResourceType.CLASS_TYPE
    );

    Map<String, Resource> result = resourceScanner.scan(getClass().getClassLoader());
    assertEquals(4560, result.size(), "Not all resources were found: " + getClass().getClassLoader());
  }

  @Test
  @Disabled
  @DisplayName("Resources must be located properly according to defined scanner's config")
  void resourcesAreLocatedProperly() throws Exception {
    ResourceScanner resourceScanner = createResourceScanner();
    Map<String, Resource> result = resourceScanner.scan(getClass().getClassLoader());
    System.out.println("Count: " + result.size());
    assertTrue(result.size() > 0, "No resource were found on: " + getClass().getClassLoader());
  }

  @Test
  @Disabled
  @DisplayName("Resources must be located properly according to defined scanner's config")
  void eachOfSelectedResourcesHasFetchableContent() throws Exception {
    ResourceScanner resourceScanner = createResourceScanner();
    Map<String, Resource> result = resourceScanner.scan(getClass().getClassLoader());
    long used = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed();

    result.forEach((resourceName, resource) -> {
      assertTrue(resource.length() > 0, resourceName);
      try {
        resource.cache();
      } catch (IOException e) {
        e.printStackTrace();
        fail(e.getMessage());
      }
    });

    System.out.printf("Used memory: %15.2f Kb\n",
                      ((ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() - used) / 1024d));
  }

  private ResourceScanner createResourceScanner() {
    return ResourceScanner.newResourceScanner(
      CONCURRENT,
      locatorsList(
        ClassLoaderResourceLocatorImpl.INSTANCE,
        ClassPathResourceLocatorImpl.INSTANCE
      ),
      resolversList(new FileResourceResolverImpl(), new JarResourceResolverImpl()),
      (resourceName, streamSupplier) ->
        resourceName.startsWith(RESOURCE_PREFIX),
      ResourceType.CLASS_TYPE);
  }
}