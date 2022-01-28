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
    // 在这里可以注册我们需要的过滤器，比如有 ThriftClient 注解
    this.addIncludeFilter(new AnnotationTypeFilter(ThriftClient.class));
  }

  @Override
  public Set<BeanDefinitionHolder> doScan(String... basePackages) {
    Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

    if (beanDefinitions.isEmpty()) {
      // 如果不存在被标识的接口，则不用处理
      if (log.isWarnEnabled()) {
        log.warn("No Thrift Client was found in {}  package. Please check your configuration.",
            Arrays.toString(basePackages));
      }
    } else {
      // 有接口，处理一下
      // 因为这个扫描出来的都是接口，Spring 容器中是对象，在这里里面处理一下接口生成对应的代理对象
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
      // 代码到这里，我们已经可以应该注入 Spring 容器中的 beanName, beanClass 以及注解上的附加信息，这里有两种不同方式将
      // 接口生成的代理对象注入的容器中
      // 第一种：直接使用 ((DefaultListableBeanFactory)applicationContext.getParentBeanFactory()).registerSingleton("beanName", 代理对象);
      // 第二种：通过设置 definition 中关于该 Bean 的生成工厂，来让 Spring 容器在合适的时候，自动通过工厂方法来调用
      //
      // 第二种可以通过两种方式来设置：
      //    第一种：通过 setBeanClass 来设置对应的工厂类，一般是 FactoryBean 及其子类
      //    第二种：通过 setFactoryBeanName 来设置对应的工厂对象，这个工程对象是已经注册到 Spring 中的
      //
      // 这里通过设置 setBeanClass 来实现的

      // 这段代码不能改顺序，这几个参数是创建下面的 BeanClass 的构造器的参数，参数顺序需要和 BeanClass 构造器里面的参数顺序和类型一致
      // 如果有空参构造器可以不传
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
    return metadata.isInterface() && metadata.hasAnnotation(ThriftClient.class.getName()) && Arrays.stream(
        metadata.getInterfaceNames()).anyMatch(it -> it.endsWith("face"));
  }

  @Override
  protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
    if (super.checkCandidate(beanName, beanDefinition)) {
      return true;
    } else {
      if (log.isWarnEnabled()) {
        log.warn("Skipping ThriftFactoryBean with name {} and {} Interface. Client already defined with the same name!",
            beanName, beanDefinition.getBeanClassName());
      }
      return false;
    }
  }

}
