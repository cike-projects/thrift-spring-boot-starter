package io.github.bw.boot.thrift.client.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ScopedProxyMode;

public class ThriftClientBeanScanProcessor implements ApplicationContextAware, BeanFactoryPostProcessor {

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    BeanDefinitionRegistry definitionRegistry = (BeanDefinitionRegistry) beanFactory;
    ClassPathThriftClientScanner beanScanner = new ClassPathThriftClientScanner(definitionRegistry);
    beanScanner.setResourceLoader(applicationContext);
    beanScanner.setBeanNameGenerator(new AnnotationBeanNameGenerator());
    beanScanner.setScopedProxyMode(ScopedProxyMode.INTERFACES);
    beanScanner.scan("");
  }
}
