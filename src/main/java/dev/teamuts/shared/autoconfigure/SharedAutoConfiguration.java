package dev.teamuts.shared.autoconfigure;

import dev.teamuts.shared.config.AppProfile;
import dev.teamuts.shared.data.EntityLockChecker;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;

@AutoConfiguration
public class SharedAutoConfiguration {
  @Bean
  @ConditionalOnMissingBean(AppProfile.class)
  public AppProfile appProfile() {
    return new AppProfile();
  }

  /** It will use the entity manager configured on each consuming project. */
  @Bean
  @ConditionalOnMissingBean(EntityLockChecker.class)
  @ConditionalOnBean(EntityManagerFactory.class)
  public EntityLockChecker entityLockChecker(EntityManagerFactory entityManagerFactory) {
    return new EntityLockChecker(
        SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory));
  }
}
