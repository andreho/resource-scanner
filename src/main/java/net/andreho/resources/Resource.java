package net.andreho.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.ReadableByteChannel;
import java.util.Optional;

/**
 * Created by a.hofmann on 09.05.2016.
 */
public interface Resource
    extends Iterable<Resource> {

  /**
   * @return a serialized representation of this resource
   */
  <V> Optional<V> getAttachment();

  /**
   * @param attachment of this resource (<b>null</b> if not used)
   */
  void setAttachment(final Object attachment);

  /**
   * @return
   */
  Optional<Resource> getNext();

  /**
   * @param resource
   */
  void setNext(Resource resource);

  /**
   * @return
   */
  boolean hasNext();

  /**
   * @return
   */
  URL getSource();

  /**
   * @return name of the referenced resource
   */
  String getName();

  /**
   * Tries to cache this resource internally.
   *
   * @return <b>true</b> if this resource was cached internally or
   * <b>false</b> if this resource can't be cached.
   */
  boolean cache()
  throws IOException;

  /**
   * Evicts a cached version of this resource if it was previously cached
   *
   * @return this
   * @throws IOException
   */
  Resource evict()
  throws IOException;

  /**
   * @return <b>true</b> if this resource is cached, <b>false</b> of not.
   */
  boolean isCached();

  /**
   * @return type of referenced resource
   */
  ResourceType getResourceType();

  /**
   * @return input stream for this resource
   * @implSpec returned stream must be closed by caller.
   */
  InputStream getInputStream()
  throws IOException;

  /**
   * @return readable byte-channel to the resource. This operation may be not supported and return an empty optional.
   * @implSpec that channel must be closed by caller.
   */
  Optional<ReadableByteChannel> getReadableByteChannel()
  throws IOException;

  /**
   * @return length of the underlying resource in bytes <u>or</u> <b>-1</b> if it isn't available.
   */
  long length();

  /**
   * @return <b>true</b> if the length of this resource is known or may be fetched, <b>false</b> otherwise.
   */
  boolean hasLength();

  /**
   * @return
   */
  byte[] toByteArray()
  throws IOException;
}
