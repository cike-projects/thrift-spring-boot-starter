package com.github.sbb.boot.thrift.server;

import org.apache.thrift.server.TServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@ConditionalOnClass(TServer.class)
public class ThriftServerAutoConfiguration {

  @ConditionalOnMissingBean
  @Bean
  public ThriftServiceDiscoverer defaultThriftServiceDiscoverer() {
    return new AnnotationThriftServiceDiscoverer();
  }
}
