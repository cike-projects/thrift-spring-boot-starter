package com.github.sbb.boot.thrift.server.autoconfigure;

import com.github.sbb.boot.thrift.server.AnnotationThriftServiceDiscoverer;
import com.github.sbb.boot.thrift.server.ThriftServerBootstrap;
import com.github.sbb.boot.thrift.server.ThriftServiceDiscoverer;
import org.apache.thrift.server.TServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@ConditionalOnClass(TServer.class)
public class ThriftServerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ThriftServerProperties thriftServerProperties() {
    return new ThriftServerProperties();
  }

  @ConditionalOnMissingBean
  @Bean
  public ThriftServiceDiscoverer defaultThriftServiceDiscoverer() {
    return new AnnotationThriftServiceDiscoverer();
  }

  @ConditionalOnMissingBean
  @ConditionalOnBean({ThriftServerProperties.class, ThriftServiceDiscoverer.class})
  @Bean
  public ThriftServerSelector defaultThriftServerSelector(ThriftServerProperties thriftServerProperties,
      ThriftServiceDiscoverer thriftServiceDiscoverer) {
    return new ThriftServerSelector(thriftServerProperties, thriftServiceDiscoverer);
  }

  @ConditionalOnMissingBean
  @ConditionalOnBean(ThriftServerSelector.class)
  @Bean
  public ThriftServerBootstrap thriftServerBootstrap(ThriftServerSelector thriftServerSelector) {
    return new ThriftServerBootstrap(thriftServerSelector);
  }

}
