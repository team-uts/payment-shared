package dev.teamuts.shared.domain.model;

import java.util.List;
import java.util.function.Function;

public abstract class BaseIdentifiableModelCollection<ID, E extends BaseIdentifiableModel<ID>> {
  private final List<E> models;

  protected BaseIdentifiableModelCollection(List<E> models) {
    this.models = List.copyOf(models);
  }

  protected final List<E> models() {
    return models;
  }

  public final int size() {
    return models.size();
  }

  public boolean isEmpty() {
    return models.isEmpty();
  }

  public boolean isNotEmpty() {
    return !isEmpty();
  }

  /** type converting from model type ({@code E}) to another type ({@code T}) */
  public <T> List<T> mapToList(Function<E, T> mapper) {
    return models.stream().map(mapper).toList();
  }
}
