package net.andreho.resources.abstr;

import net.andreho.resources.Resource;
import net.andreho.resources.ResourceType;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by a.hofmann on 09.05.2016.
 */
public abstract class AbstractResource<T>
    implements Resource {

  private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
  static final int TEMP_BUFFER_CAPACITY = 256;
  private final URL source;
  private final String name;
  private final ResourceType resourceType;

  protected volatile Object attachment;
  protected volatile Resource next;
  protected volatile Reference<byte[]> cachedReference;

  public AbstractResource(final URL source,
                          final String name,
                          final ResourceType resourceType) {
    this.source = Objects.requireNonNull(source, "source");
    this.name = Objects.requireNonNull(name, "name");
    this.resourceType = Objects.requireNonNull(resourceType, "resourceType");
  }

  @Override
  public Optional<Resource> getNext() {
    final Resource next = this.next;
    return next == null? Optional.empty() : Optional.of(next);
  }

  @Override
  public void setNext(final Resource resource) {
    this.next = resource;
  }

  @Override
  public boolean hasNext() {
    return this.next != null;
  }

  @Override
  public boolean cache()
  throws IOException {
    return false;
  }

  @Override
  public URL getSource() {
    return this.source;
  }

  @Override
  public <V> Optional<V> getAttachment() {
    final Object attachment = this.attachment;
    return attachment == null? Optional.empty() : Optional.of((V) attachment);
  }

  @Override
  public void setAttachment(final Object attachment) {
    this.attachment = attachment;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Resource evict()
  throws IOException {
    this.cachedReference = null;
    return this;
  }

  @Override
  public boolean isCached() {
    Reference<byte[]> cachedReference = this.cachedReference;
    return cachedReference != null && cachedReference.get() != null;
  }

  @Override
  public ResourceType getResourceType() {
    return this.resourceType;
  }

  @Override
  public Optional<ReadableByteChannel> getReadableByteChannel()
  throws IOException {
    return openReadableByteChannel();
  }

  @Override
  public InputStream getInputStream()
  throws IOException {
    return openInputStream();
  }

  @Override
  public long length() {
    return -1;
  }

  @Override
  public boolean hasLength() {
    return false;
  }

  @Override
  public Iterator<Resource> iterator() {
    return new Iterator<Resource>() {
      private Resource current = AbstractResource.this;

      @Override
      public boolean hasNext() {
        return this.current != null;
      }

      @Override
      public Resource next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        Resource current = this.current;
        this.current = current.getNext().orElse(null);
        return current;
      }
    };
  }

  /**
   * Used to open an instance of {@link ReadableByteChannel}
   *
   * @return an instance (not null)
   * @throws IOException
   */
  protected Optional<ReadableByteChannel> openReadableByteChannel()
  throws IOException {
    return Optional.empty();
  }

  /**
   * Opens the underlying stream to the referenced resource
   *
   * @return an opened stream that <u>must</u> be closed by user again.
   * @throws IOException
   */
  protected abstract InputStream openInputStream()
  throws IOException;

  @Override
  public String toString() {
    return getName();
  }
}
