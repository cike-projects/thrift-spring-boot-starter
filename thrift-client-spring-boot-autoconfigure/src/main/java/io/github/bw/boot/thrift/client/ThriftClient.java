package io.github.bw.boot.thrift.client;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ThriftClient {

  String name() default "";

  String serviceId();

  Class<?> fallback() default void.class;

  Class<?> fallbackBean() default void.class;

  String fallbackBeanName() default "";

}
