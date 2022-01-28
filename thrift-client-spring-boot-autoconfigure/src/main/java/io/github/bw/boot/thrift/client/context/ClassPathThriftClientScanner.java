package io.github.bw.boot.thrift.client.context;

import io.github.bw.boot.thrift.client.ThriftClient;
import java.util.Arrays;
import java.util.Set;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

@Slf4j
public class ClassPathThriftClientScanner extends ClassPathBeanDefinitionScanner {

  public ClassPathThriftClientScanner(BeanDefinitionRegistry registry) {
    super(registry);
  }

  @Override
  protected void registerDefaultFilters() {
    this.addIncludeFilter(new AnnotationTypeFilter(ThriftClient.class));
  }

  @Override
  public Set<BeanDefinitionHolder> doScan(String... basePackages) {
    Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

    if (beanDefinitions.isEmpty()) {
      if (log.isWarnEnabled()) {
        log.warn("No Thrift Client was found in {}  package. Please check your configuration.",
            Arrays.toString(basePackages));
      }
    } else {
      processBeanDefinitions(beanDefinitions);
    }

    return beanDefinitions;
  }

  @SneakyThrows
  private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
    for (BeanDefinitionHolder beanDefinitionHolder : beanDefinitions) {
      GenericBeanDefinition definition = (GenericBeanDefinition) beanDefinitionHolder.getBeanDefinition();
      log.info("Thrift Client beanName: {}, BeanClassName: {}", beanDefinitionHolder.getBeanName(),
          definition.getBeanClassName());
      Class<?> beanClass = Class.forName(definition.getBeanClassName());

      ThriftClient thriftClient = AnnotationUtils.findAnnotation(beanClass, ThriftClient.class);

      // 这段代码不能改顺序
      ConstructorArgumentValues constructorArgumentValues = definition.getConstructorArgumentValues();
      constructorArgumentValues.addGenericArgumentValue(thriftClient.serviceId());
      constructorArgumentValues.addGenericArgumentValue(thriftClient.name());
      constructorArgumentValues.addGenericArgumentValue(beanClass);

      definition.setBeanClass(ThriftClientFactoryBean.class);
    }
  }

  @Override
  protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
    AnnotationMetadata metadata = beanDefinition.getMetadata();
    return metadata.isInterface()
        && metadata.hasAnnotation(ThriftClient.class.getName())
        && Arrays.stream(metadata.getInterfaceNames()).anyMatch(it -> it.endsWith("face"));
  }

  @Override
  protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
    if (super.checkCandidate(beanName, beanDefinition)) {
      return true;
    } else {
      if (log.isWarnEnabled()) {
        log.warn("Skipping ThriftFactoryBean with name {} and {} Interface. Client already defined with the same name!",
            beanName,
            beanDefinition.getBeanClassName());
      }
      return false;
    }
  }

}
