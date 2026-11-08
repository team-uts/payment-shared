package dev.teamuts.shared.domain.model;

import dev.teamuts.shared.data.PersistenceException;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseIdentifiableModel<ID> {
  @EqualsAndHashCode.Include private final ID id;

  protected BaseIdentifiableModel(ID id) {
    if (id != null && !(id instanceof Long || id instanceof UUID)) {
      throw new IllegalArgumentException("ID must be Long or UUID");
    }

    this.id = id;
  }

  public final void checkPersisted() {
    if (!isPersisted()) {
      throw PersistenceException.notPersisted(this);
    }
  }

  private boolean isPersisted() {
    return switch (id) {
      case Number numId -> numId.longValue() > 0;
      // TODO: need to check
      case UUID uuid -> !uuid.equals(new UUID(0L, 0L));
      case null, default -> false;
    };
  }
}
