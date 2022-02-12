package io.github.bw.boot.thrift.client.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ScopedProxyMode;

@Slf4j
public class ThriftClientBeanScanProcessor implements BeanDefinitionRegistryPostProcessor, InitializingBean,
    ApplicationContextAware {

  private ApplicationContext applicationContext;

  private String[] basePackages;

  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("afterPropertiesSet");
  }

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    ClassPathThriftClientScanner beanScanner = new ClassPathThriftClientScanner(registry, applicationContext);
    beanScanner.setResourceLoader(applicationContext);
    beanScanner.setBeanNameGenerator(new AnnotationBeanNameGenerator());
    beanScanner.setScopedProxyMode(ScopedProxyMode.INTERFACES);
    beanScanner.scan(basePackages);
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    // pass
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public String[] getBasePackages() {
    return basePackages;
  }

  public void setBasePackages(String[] basePackages) {
    this.basePackages = basePackages;
  }
}
