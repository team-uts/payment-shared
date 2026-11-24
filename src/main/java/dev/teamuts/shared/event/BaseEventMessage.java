package dev.teamuts.shared.event;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class BaseEventMessage {
  @EqualsAndHashCode.Include private Long identifier;
}
