package io.github.bw.boot.thrift.client.context;

import io.github.bw.boot.thrift.client.annotation.ThriftClientScan;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

public class ThriftClientRegistrar implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

    AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(
        importingClassMetadata.getAnnotationAttributes(ThriftClientScan.class.getName()));

    List<String> basePackages = new ArrayList<>();
    for (String pkg : annoAttrs.getStringArray("value")) {
      if (StringUtils.hasText(pkg)) {
        basePackages.add(pkg);
      }
    }

    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ThriftClientBeanScanProcessor.class);
    builder.addPropertyValue("basePackages", StringUtils.toStringArray(basePackages));
    builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    registry.registerBeanDefinition("thriftClientBeanScanProcessor", builder.getBeanDefinition());
  }

}
