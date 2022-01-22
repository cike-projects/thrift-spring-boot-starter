package com.github.sbb.boot.thrift.server;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

public class AnnotationThriftServiceDiscoverer implements ApplicationContextAware, ThriftServiceDiscoverer {

  private static final Logger logger = LoggerFactory.getLogger(AnnotationThriftServiceDiscoverer.class);

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.findThriftServices();
  }

  @Override
  public List<ThriftServiceDefinition> findThriftServices() {

    String[] beanNames = applicationContext.getBeanNamesForAnnotation(ThriftService.class);
    if (beanNames.length == 0) {
      logger.error("Can't search any thrift service annotated with @ThriftService");
      throw new RuntimeException("Can not found any thrift service");
    }

    return Arrays.stream(beanNames).distinct().map(beanName -> {
      Object bean = applicationContext.getBean(beanName);
      ThriftService thriftService = bean.getClass().getAnnotation(ThriftService.class);
      String thriftServiceName =
          StringUtils.isEmpty(thriftService.value()) ? bean.getClass().getName() : thriftService.value();
      return new ThriftServiceDefinition(thriftServiceName, bean);
    }).collect(Collectors.toList());
  }
}
