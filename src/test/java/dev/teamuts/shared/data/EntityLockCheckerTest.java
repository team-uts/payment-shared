package dev.teamuts.shared.data;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import dev.teamuts.shared.domain.model.BaseIdentifiableModel;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@SpringJUnitConfig(TestEntityConfig.class)
@Transactional
class EntityLockCheckerTest {
  private static final Long TEST_ENTITY_ID = 100L;

  @Autowired private EntityManager entityManager;
  @Autowired private EntityLockChecker checker;
  @Autowired private PlatformTransactionManager transactionManager;
  private final TestModel model = new TestModel(TEST_ENTITY_ID);

  /**
   * flush() and clear() don’t release the insertion’s database locks.<br>
   * That is why the current design uses @BeforeTransaction and explicit cleanup afterward.
   */
  @BeforeTransaction
  void createTestRow() {
    // Persist before the test so its database lock cannot affect the lock check.
    new TransactionTemplate(transactionManager)
        .executeWithoutResult(status -> entityManager.persist(new LockTestEntity(TEST_ENTITY_ID)));
  }

  @AfterTransaction
  void deleteTestRow() {
    // The fixture was committed separately, so the test's rollback does not remove it.
    new TransactionTemplate(transactionManager)
        .executeWithoutResult(status -> {
          LockTestEntity entity = entityManager.find(LockTestEntity.class, TEST_ENTITY_ID);
          if (entity != null) {
            entityManager.remove(entity);
          }
        });
  }

  @ParameterizedTest
  @EnumSource(
      value = LockModeType.class,
      names = {"PESSIMISTIC_READ", "PESSIMISTIC_WRITE", "PESSIMISTIC_FORCE_INCREMENT"})
  void recognizesLockAcquiredByJpa(LockModeType mode) {
    entityManager.find(LockTestEntity.class, model.getId(), mode);

    assertTrue(checker.isLocked(model, LockTestEntity.class));
    assertDoesNotThrow(() -> checker.requireLocked(model, LockTestEntity.class));
  }

  @ParameterizedTest
  @EnumSource(
      value = LockModeType.class,
      names = {"NONE", "READ", "WRITE", "OPTIMISTIC", "OPTIMISTIC_FORCE_INCREMENT"})
  void doesNotTreatOptimisticModeAsDatabaseLock(LockModeType mode) {
    entityManager.find(LockTestEntity.class, model.getId(), mode);

    assertFalse(checker.isLocked(model, LockTestEntity.class));
    assertThatThrownBy(() -> checker.requireLocked(model, LockTestEntity.class))
        .isInstanceOf(PersistenceException.class)
        .hasMessageContaining(PersistenceErrorCode.NOT_ACQUIRED_LOCK.name());
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void doesNotCarryLockIntoNextTransaction(boolean commit) {
    entityManager.find(LockTestEntity.class, model.getId(), LockModeType.PESSIMISTIC_WRITE);
    assertTrue(checker.isLocked(model, LockTestEntity.class));
    if (commit) {
      TestTransaction.flagForCommit();
    } else {
      TestTransaction.flagForRollback();
    }
    TestTransaction.end();

    assertFalse(checker.isLocked(model, LockTestEntity.class));
    TestTransaction.start();
    assertFalse(checker.isLocked(model, LockTestEntity.class));
  }

  @Test
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  void returnsFalseWithoutTransaction() {
    assertFalse(checker.isLocked(model, LockTestEntity.class));
    assertThatThrownBy(() -> checker.requireLocked(model, LockTestEntity.class))
        .isInstanceOf(PersistenceException.class)
        .hasMessageContaining(PersistenceErrorCode.NOT_ACQUIRED_LOCK.name());
  }

  @Test
  void missingRowIsNotLocked() {
    assertFalse(checker.isLocked(new TestModel(2L), LockTestEntity.class));
  }

  @Test
  void checkingDoesNotAcquireLock() {
    assertFalse(checker.isLocked(model, LockTestEntity.class));
    assertThat(entityManager.getLockMode(entityManager.find(LockTestEntity.class, model.getId())))
        .isEqualTo(LockModeType.NONE);
  }

  // model
  private static class TestModel extends BaseIdentifiableModel<Long> {
    TestModel(Long id) {
      super(id);
    }
  }

  // jpa entity
  @Entity(name = "LockTestEntity")
  public static class LockTestEntity {
    @Id private Long id;

    // for testing OPTIMISTIC, OPTIMISTIC_FORCE_INCREMENT, PESSIMISTIC_FORCE_INCREMENT
    @Version private long version;

    protected LockTestEntity() {}

    LockTestEntity(Long id) {
      this.id = id;
    }
  }
}
