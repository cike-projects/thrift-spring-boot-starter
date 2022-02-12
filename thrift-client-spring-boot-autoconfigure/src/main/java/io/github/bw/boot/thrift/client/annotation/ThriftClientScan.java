package io.github.bw.boot.thrift.client.annotation;

import io.github.bw.boot.thrift.client.context.ThriftClientRegistrar;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(ThriftClientRegistrar.class)
public @interface ThriftClientScan {

    String[] value() default {};
}
