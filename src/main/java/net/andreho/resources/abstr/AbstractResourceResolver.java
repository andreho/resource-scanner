package net.andreho.resources.abstr;

import net.andreho.resources.Resource;
import net.andreho.resources.ResourceFilter;
import net.andreho.resources.ResourceResolver;
import net.andreho.resources.ResourceTypeSelector;
import net.andreho.resources.impl.Utils;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * <br/>Created by a.hofmann on 16.06.2017 at 03:46.
 */
public abstract class AbstractResourceResolver
    implements ResourceResolver {

  protected boolean isSubJar(final URL url, final String resourceName) {
    return Utils.endsWithCaseInsensitive(resourceName, ".jar");
  }

  protected Map<String, Resource> scanSubJar(final URL url,
                                             final File file,
                                             final ResourceFilter resourceFilter,
                                             final ResourceTypeSelector typeSelector) {

    return Collections.emptyMap();
  }

  protected Map<String, Resource> scanSubJar(final URL url,
                                             final JarFile jarFile,
                                             final JarEntry jarEntry,
                                             final ResourceFilter resourceFilter,
                                             final ResourceTypeSelector typeSelector) {

    return Collections.emptyMap();
  }
}
