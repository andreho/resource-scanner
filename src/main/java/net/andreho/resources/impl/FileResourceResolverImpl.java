package net.andreho.resources.impl;

import net.andreho.resources.Resource;
import net.andreho.resources.ResourceFilter;
import net.andreho.resources.ResourceType;
import net.andreho.resources.ResourceTypeSelector;
import net.andreho.resources.abstr.AbstractResourceResolver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static net.andreho.resources.impl.Utils.mergeResults;

/**
 * Created by a.hofmann on 13.05.2016.
 */
public class FileResourceResolverImpl extends AbstractResourceResolver {

  private static final String FILE_PROTOCOL_NAME = "file";

  @Override
  public Map<String, Resource> resolve(final URL url,
                                       final ResourceFilter resourceFilter,
                                       final ResourceTypeSelector typeSelector) {

    if (!FILE_PROTOCOL_NAME.equalsIgnoreCase(url.getProtocol())) {
      return Collections.emptyMap();
    }

    try {
      final File directory = new File(url.toURI());
      return scan(url, directory, resourceFilter, typeSelector);
    } catch (URISyntaxException e) {
      throw new IllegalStateException("Unable to resolve given URL: " + url, e);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to process directory, that is referenced by: " + url, e);
    }
  }

  protected Map<String, Resource> scan(final URL url,
                                       final File file,
                                       final ResourceFilter resourceFilter,
                                       final ResourceTypeSelector typeSelector)
  throws IOException {
    if (!file.exists()) {
      return Collections.emptyMap();
    }

    final Path targetPath = file.toPath();
    final Map<String, Resource> result = new LinkedHashMap<>();

    Files.walkFileTree(targetPath,
                       new FileResourcePathVisitor(this, url, targetPath, resourceFilter, typeSelector, result));

    if(isSubJar(url, file.getName())) {
      mergeResults(result, scanSubJar(url, file, resourceFilter, typeSelector));
    }

    return Collections.unmodifiableMap(result);
  }

  protected boolean filterFile(final Path file,
                               final String resourceName,
                               final ResourceFilter resourceFilter) {

    try (final InputStreamSupplier streamSupplier =
           new InputStreamSupplier(() -> Files.newInputStream(file, StandardOpenOption.READ))) {

      return resourceFilter.filter(resourceName, streamSupplier);
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }


  protected Resource createResource(final URL url,
                                    final String resourceName,
                                    final File target,
                                    final ResourceTypeSelector typeSelector) {

    final ResourceType resourceType = typeSelector.select(resourceName);
    return new FileResourceImpl(url, resourceName, resourceType, target);
  }
}
