package dev.teamuts.shared.autoconfigure;

import dev.teamuts.shared.config.AppProfile;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class SharedAutoConfiguration {
  @Bean
  @ConditionalOnMissingBean(AppProfile.class)
  public AppProfile appProfile() {
    return new AppProfile();
  }
}
