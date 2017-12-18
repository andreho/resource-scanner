package net.andreho.resources;

import net.andreho.resources.impl.ClassLoaderResourceLocatorImpl;
import net.andreho.resources.impl.ClassPathResourceLocatorImpl;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Created by a.hofmann on 13.05.2016.
 */
@FunctionalInterface
public interface ResourceSourceLocator {

  /**
   * @param classLoader to inspect for possible resource sources
   */
  Collection<URL> locateResources(ClassLoader classLoader);

  /**
   * @param resourceSourceLocators to merge
   * @return a merged version that uses all given locators as source
   */
  static ResourceSourceLocator locatorsList(final ResourceSourceLocator... resourceSourceLocators) {
    return locatorsList(Arrays.asList(resourceSourceLocators));
  }

  /**
   * @param resourceSourceLocators  to merge
   * @return a merged version that uses all given locators as source
   */
  static ResourceSourceLocator locatorsList(final Collection<ResourceSourceLocator> resourceSourceLocators) {
    final ResourceSourceLocator[] locators = resourceSourceLocators.toArray(new ResourceSourceLocator[0]);

    return (classLoader) -> {
      final Collection<URL> urls = new LinkedHashSet<>();
      for (ResourceSourceLocator locator : locators) {
        urls.addAll(locator.locateResources(classLoader));
      }
      return urls;
    };
  }

  /**
   * @return evaluates resources given by class-path JVM's parameter
   */
  static ResourceSourceLocator usingClassPath() {
    return new ClassPathResourceLocatorImpl();
  }

  /**
   * @return evaluates resources given by provided class-loader
   */
  static ResourceSourceLocator usingGivenClassLoader() {
    return new ClassLoaderResourceLocatorImpl();
  }
}
