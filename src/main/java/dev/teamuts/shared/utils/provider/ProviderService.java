package dev.teamuts.shared.utils.provider;

public interface ProviderService<T extends Enum<T>> {
  boolean supports(T key);
}
