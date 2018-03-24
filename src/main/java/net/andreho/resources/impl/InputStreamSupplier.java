package net.andreho.resources.impl;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * <br/>Created by a.hofmann on 16.06.2017 at 03:20.
 */
public final class InputStreamSupplier implements Supplier<InputStream>, Closeable {

  private final Callable<InputStream> inputStreamSupplier;
  private volatile InputStream inputStream;
  private volatile boolean closed;

  public InputStreamSupplier(final Callable<InputStream> inputStreamSupplier) {
    this.inputStreamSupplier = Objects.requireNonNull(inputStreamSupplier);
  }

  @Override
  public void close()
  throws IOException {
    if (!this.closed) {
      this.closed = true;
      try {
        if (this.inputStream != null) {
          this.inputStream.close();
        }
      } finally {
        this.inputStream = null;
      }
    }
  }

  @Override
  public InputStream get() {
    if (this.closed) {
      throw new IllegalStateException("Stream was already closed.");
    }
    try {
      return this.inputStream = inputStreamSupplier.call();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
