package io.bit3.jsass.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Name {

  /**
   * The parameter name.
   *
   * <p>Defines the parameter name used in the sass language for named parameters.</p>
   */
  String value();
}
