package dev.teamuts.shared.data;

import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.JdbcSettings;
import org.hibernate.cfg.SchemaToolingSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration(proxyBeanMethods = false)
public class TestEntityConfig {
  @Bean(destroyMethod = "close")
  SessionFactory sessionFactory() {
    return new org.hibernate.cfg.Configuration()
        .addAnnotatedClass(EntityLockCheckerTest.LockTestEntity.class)
        .setProperty(JdbcSettings.JAKARTA_JDBC_DRIVER, "org.h2.Driver")
        .setProperty(JdbcSettings.JAKARTA_JDBC_URL, "jdbc:h2:mem:entity-lock-checker")
        .setProperty(SchemaToolingSettings.HBM2DDL_AUTO, "create-drop")
        .buildSessionFactory();
  }

  /**
   * - Before test → Begin transaction <br>
   * - Run test → Use its EntityManager <br>
   * - After test → Roll back transaction
   */
  @Bean
  PlatformTransactionManager transactionManager(SessionFactory sessionFactory) {
    return new JpaTransactionManager(sessionFactory);
  }

  /**
   * Injected EntityManager proxy <br>
   * - Transaction A → EntityManager A <br>
   * - Transaction B → EntityManager B
   */
  @Bean
  EntityManager entityManager(SessionFactory sessionFactory) {
    return SharedEntityManagerCreator.createSharedEntityManager(sessionFactory);
  }

  @Bean
  EntityLockChecker entityLockChecker(EntityManager entityManager) {
    return new EntityLockChecker(entityManager);
  }
}
