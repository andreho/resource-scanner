package net.andreho.resources.impl;

import net.andreho.resources.ResourceType;
import net.andreho.resources.abstr.AbstractByteArrayResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by a.hofmann on 09.05.2016.
 */
public class FileResourceImpl extends AbstractByteArrayResource {

  private final File file;

  public FileResourceImpl(final URL source,
                          final String name,
                          final ResourceType resourceType,
                          final File file) {

    super(source, name, resourceType);
    this.file = Objects.requireNonNull(file, "file");
    assert file.exists() : "Given file doesn't exist.";
  }

  @Override
  protected FileInputStream openInputStream() throws IOException {
    return new FileInputStream(this.file);
  }

  @Override
  protected Optional<ReadableByteChannel> openReadableByteChannel() throws IOException {
    return Optional.of(openInputStream().getChannel());
  }

  @Override
  public boolean hasLength() {
    return true;
  }

  @Override
  public long length() {
    return file.length();
  }
}
