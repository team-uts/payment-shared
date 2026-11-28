package dev.teamuts.shared.domain.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ModelSourceType {
  LOGIC("from business logic", false, false),
  DATABASE("from database", true, true),
  CACHE("from cache", true, false),
  ;

  private final String description;
  private final boolean persistence;
  private final boolean strictlyPersistent;
}
