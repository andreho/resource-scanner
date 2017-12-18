package net.andreho.resources.impl;

import net.andreho.resources.ResourceSourceLocator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashSet;

import static net.andreho.resources.impl.Utils.endsWithCaseInsensitive;

/**
 * Created by a.hofmann on 13.05.2016.
 */
public class ClassPathResourceLocatorImpl
    implements ResourceSourceLocator {

  public static final ResourceSourceLocator INSTANCE = new ClassPathResourceLocatorImpl();
  private static final String JAVA_CLASS_PATH = "java.class.path";

  @Override
  public Collection<URL> locateResources(final ClassLoader classLoader) {
    final String[] classpath = System.getProperty(JAVA_CLASS_PATH).split(File.pathSeparator);
    final Collection<URL> result = new LinkedHashSet<>();
    for (String file : classpath) {
      result.add(classPathFileAsUrl(file));
    }
    return result;
  }

  protected URL classPathFileAsUrl(String file) {
    URL url;
    file = file.replace(File.separatorChar, '/');
    try {
      if (endsWithCaseInsensitive(file, ".jar")) {
        url = new URL("jar", "", "file:/" + file);
      } else {
        if (new File(file).isDirectory() && !file.endsWith("/")) {
          file += "/";
        }
        url = new URL("file", "", "/" + file);
      }
    } catch (MalformedURLException e) {
      throw new IllegalStateException("Unable to transform given file to an URL: " + file, e);
    }
    return url;
  }
}
