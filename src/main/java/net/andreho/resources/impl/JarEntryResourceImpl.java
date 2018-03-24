package net.andreho.resources.impl;

import net.andreho.resources.ResourceType;
import net.andreho.resources.abstr.AbstractByteArrayResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by a.hofmann on 09.05.2016.
 */
public class JarEntryResourceImpl extends AbstractByteArrayResource {

  private final File jarFile;
  private final long length;

  public JarEntryResourceImpl(final URL source,
                              final String name,
                              final ResourceType resourceType,
                              final File jarFile,
                              final long length) {

    super(source, name, resourceType);
    this.jarFile = Objects.requireNonNull(jarFile, "jarFile");
    this.length = length;
  }

  @Override
  protected InputStream openInputStream() throws IOException {

    final ZipFile zipFile = new ZipFile(this.jarFile);
    final ZipEntry entry = zipFile.getEntry(getName());

    if (entry == null) {
      throw new IllegalStateException("Resource's entry isn't available anymore: " + this.jarFile + "/!" + getName());
    }

    return zipFile.getInputStream(entry);
  }

  @Override
  public long length() {
    return length;
  }

  @Override
  public boolean hasLength() {
    return length > -1;
  }
}
