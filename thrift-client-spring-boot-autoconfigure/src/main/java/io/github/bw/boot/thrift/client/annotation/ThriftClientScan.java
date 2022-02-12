package io.github.bw.boot.thrift.client.annotation;

import io.github.bw.boot.thrift.client.context.ThriftClientBeanScanProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(ThriftClientBeanScanProcessor.class)
public @interface ThriftClientScan {

    String[] value() default {};
}
