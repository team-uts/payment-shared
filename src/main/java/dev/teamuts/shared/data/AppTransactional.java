package dev.teamuts.shared.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(transactionManager = TransactionConstants.APP_TRANSACTION_MANAGER)
public @interface AppTransactional {

  /**
   * @see Transactional#readOnly()
   */
  @AliasFor(attribute = "readOnly", annotation = Transactional.class)
  boolean readOnly() default false;

  /**
   * @see Transactional#propagation()
   */
  @AliasFor(attribute = "propagation", annotation = Transactional.class)
  Propagation propagation() default Propagation.REQUIRED;
}
