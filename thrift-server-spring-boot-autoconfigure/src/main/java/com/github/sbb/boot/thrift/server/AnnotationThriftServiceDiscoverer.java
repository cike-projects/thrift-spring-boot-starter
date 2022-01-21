package com.github.sbb.boot.thrift.server;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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

    Map<String, Object> thriftServiceMap = applicationContext.getBeansWithAnnotation(ThriftService.class);

    for (Entry<String, Object> entry : thriftServiceMap.entrySet()) {
      logger.debug("Found Thrift service: {}, bean: {}, class: {}", entry.getKey(), entry.getKey(),
          entry.getValue().getClass().getName());
    }
    return null;
  }
}
