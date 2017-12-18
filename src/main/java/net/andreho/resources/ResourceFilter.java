package net.andreho.resources;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Created by a.hofmann on 10.05.2016.
 */
@FunctionalInterface
public interface ResourceFilter {

  /**
   * @param resourceName of the actual resource
   * @return <b>true</b> if given resource should be collected or <b>false</b> otherwise.
   * @implNote the supplied stream is closed automatically after this method
   */
  boolean filter(String resourceName,
                 Supplier<InputStream> streamSupplier);

  /**
   * @return an instance of resource filter that accepts any resources
   */
  static ResourceFilter withoutFiltering() {
    return (resourceName, streamSupplier) -> true;
  }
}
