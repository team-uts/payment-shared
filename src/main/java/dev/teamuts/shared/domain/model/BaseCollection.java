package dev.teamuts.shared.domain.model;

import java.util.List;
import java.util.function.Function;

public abstract class BaseCollection<E> {
  private final List<E> elements;

  protected BaseCollection(List<E> elements) {
    this.elements = elements;
  }

  protected List<E> elements() {
    return elements;
  }

  public final int size() {
    return elements.size();
  }

  public boolean isEmpty() {
    return elements.isEmpty();
  }

  public boolean isNotEmpty() {
    return !isEmpty();
  }

  /** type converting from model type ({@code E}) to another type ({@code T}) */
  public <R> List<R> mapToList(Function<E, R> mapper) {
    return elements.stream().map(mapper).toList();
  }
}
