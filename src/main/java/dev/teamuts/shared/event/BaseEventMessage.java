package dev.teamuts.shared.event;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseEventMessage {
  @EqualsAndHashCode.Include private Long identifier;
}
