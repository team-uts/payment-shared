package dev.teamuts.shared.domain.model;

import java.util.List;

public abstract class BaseIdentifiableModelCollection<ID, E extends BaseIdentifiableModel<ID>>
    extends BaseCollection<E> {
  protected BaseIdentifiableModelCollection(List<E> models) {
    super(models);
  }
}
