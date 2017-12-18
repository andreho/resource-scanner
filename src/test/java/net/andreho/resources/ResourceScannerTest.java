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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


/**
 * Created by a.hofmann on 20.05.2016.
 */
public class ResourceScannerTest {
  private static final String RESOURCE_PREFIX = "java/";

  @Test
  @Disabled
  @DisplayName("Resources must be located properly according to defined scanner's config")
  public void resourcesAreLocatedProperly()
  throws Exception {
    ResourceScanner resourceScanner = createResourceScanner();
    Map<String, Resource> result = resourceScanner.scan(getClass().getClassLoader());
    System.out.println("Count: " + result.size());
    assertTrue(result.size() > 0, "No resource were found on: "+getClass().getClassLoader());
  }

  @Test
  @Disabled
  @DisplayName("Resources must be located properly according to defined scanner's config")
  public void eachOfSelectedResourcesHasFetchableContent()
  throws Exception {
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
    return ResourceScanner.newScanner(
        ResourceScanner.Parallelism.CONCURRENT,
        ResourceSourceLocator.locatorsList(
            ClassLoaderResourceLocatorImpl.INSTANCE,
            ClassPathResourceLocatorImpl.INSTANCE
        ),
        ResourceResolver.with(new FileResourceResolverImpl(), new JarResourceResolverImpl()),
        (resourceName, streamSupplier) ->
          resourceName.startsWith(RESOURCE_PREFIX),
        ResourceType.CLASS_TYPE);
  }
}