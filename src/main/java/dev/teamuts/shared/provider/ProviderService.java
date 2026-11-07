package dev.teamuts.shared.provider;

public interface ProviderService<T extends Enum<T>> {
  boolean supports(T key);
}
