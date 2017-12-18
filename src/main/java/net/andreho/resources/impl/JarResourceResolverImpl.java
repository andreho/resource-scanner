package net.andreho.resources.impl;

import net.andreho.resources.Resource;
import net.andreho.resources.ResourceFilter;
import net.andreho.resources.ResourceType;
import net.andreho.resources.ResourceTypeSelector;
import net.andreho.resources.abstr.AbstractResourceResolver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.andreho.resources.impl.Utils.mergeResults;

/**
 * Created by a.hofmann on 13.05.2016.
 */
public class JarResourceResolverImpl
    extends AbstractResourceResolver {

  private static final Pattern JAR_PATTERN =
      Pattern.compile("(?:jar:file:[/\\\\])?(.*\\.jar)(?:[!/]*)?", Pattern.CASE_INSENSITIVE);
  private static final String JAR_PROTOCOL_NAME = "jar";

  @Override
  public Optional<Map<String, Resource>> resolve(final URL url,
                                                 final ResourceFilter nameFilter,
                                                 final ResourceTypeSelector selector) {

    if (JAR_PROTOCOL_NAME.equalsIgnoreCase(url.getProtocol())) {
      final Matcher matcher = JAR_PATTERN.matcher(url.toString());

      if (!matcher.matches()) {
        throw new IllegalStateException("Unable to process a JAR file at the given URL: " + url);
      }

      try {
        final File jar = new File(matcher.group(1));
        return scan(url, jar, nameFilter, selector);
      } catch (IOException e) {
        throw new IllegalStateException("Unable to process directory, that is referenced by: " + url, e);
      }
    }
    return Optional.empty();
  }

  protected Optional<Map<String, Resource>> scan(final URL url,
                                                 final File jar,
                                                 final ResourceFilter resourceFilter,
                                                 final ResourceTypeSelector typeSelector)
  throws IOException {
    if (!jar.exists()) {
      return Optional.empty();
    }

    final Map<String, Resource> result = new HashMap<>();

    try (final JarFile jarFile = new JarFile(jar)) {
      for (Enumeration<JarEntry> enumeration = jarFile.entries(); enumeration.hasMoreElements(); ) {
        final JarEntry jarEntry = enumeration.nextElement();

        if (!jarEntry.isDirectory()) {
          String resourceName = jarEntry.getName();
          if (filterResource(jarFile, jarEntry, resourceName, resourceFilter)) {
            result.put(resourceName, createResource(url, resourceName, jar, typeSelector, jarEntry.getSize()));
          }

          if (isSubJar(url, resourceName)) {
            mergeResults(result, scanSubJar(url, jarFile, jarEntry, resourceFilter, typeSelector));
          }
        }
      }
    } catch (IOException e) {
      throw new IllegalStateException("Unable to open/read/close the given jar file: " + jar.getAbsolutePath(), e);
    }
    return Optional.of(result);
  }

  private boolean filterResource(final JarFile jarFile,
                                 final JarEntry jarEntry,
                                 final String resourceName,
                                 final ResourceFilter resourceFilter) {

    try (final InputStreamSupplier streamSupplier = new InputStreamSupplier(() -> jarFile.getInputStream(jarEntry))) {
      return resourceFilter.filter(resourceName, streamSupplier);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  protected Resource createResource(final URL url,
                                    final String resourceName,
                                    final File jar,
                                    final ResourceTypeSelector selector,
                                    final long length) {
    final ResourceType resourceType = selector.select(resourceName);
    return new JarEntryResourceImpl(url, resourceName, resourceType, jar, length);
  }
}
