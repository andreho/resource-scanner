package net.andreho.resources.impl;

import net.andreho.resources.Resource;
import net.andreho.resources.ResourceFilter;
import net.andreho.resources.ResourceTypeSelector;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

/**
 * <br/>Created by a.hofmann on 17.06.2017 at 04:27.
 */
class FileResourcePathVisitor implements FileVisitor<Path> {

  private final URL url;
  private final Path target;
  private final ResourceFilter resourceFilter;
  private final ResourceTypeSelector selector;
  private final Map<String, Resource> result;
  private final StringBuilder stack;
  private final FileResourceResolverImpl fileResourceResolver;

  public FileResourcePathVisitor(final FileResourceResolverImpl fileResourceResolver,
                                 final URL url,
                                 final Path target,
                                 final ResourceFilter resourceFilter,
                                 final ResourceTypeSelector selector,
                                 final Map<String, Resource> result) {

    this.fileResourceResolver = fileResourceResolver;
    this.target = target;
    this.resourceFilter = resourceFilter;
    this.result = result;
    this.url = url;
    this.selector = selector;
    this.stack = new StringBuilder(128);
  }

  @Override
  public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
    if (!target.equals(dir)) {
      stack.append(dir.getFileName().toString()).append('/');
    }
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {
    if (!target.equals(dir)) {
      stack.setLength(stack.length() - (dir.getFileName().toString().length() + 1));
    }
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
    final String fileName = file.getFileName().toString();
    final StringBuilder stack = this.stack;

    try {
      final String resourceName = stack.append(fileName).toString();
      if (fileResourceResolver.filterFile(file, resourceName, resourceFilter)) {
        result.put(resourceName, fileResourceResolver.createResource(url, resourceName, file.toFile(), selector));
      }
    } finally {
      stack.setLength(stack.length() - fileName.length());
    }
    return FileVisitResult.CONTINUE;
  }

  @Override
  public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
    throw exc;
  }
}
