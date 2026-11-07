package dev.teamuts.shared.provider;

import java.util.List;

public abstract class Provider<T extends Enum<T>, S extends ProviderService<T>> {
  private final List<S> services;

  protected Provider(List<S> services) {
    this.services = services;
  }

  public S getInstance(T key) {
    return services.stream()
        .filter(service -> service.supports(key))
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException("service not found with key %s".formatted(key.name())));
  }
}
