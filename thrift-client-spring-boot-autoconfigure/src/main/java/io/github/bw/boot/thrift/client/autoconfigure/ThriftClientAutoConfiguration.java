package io.github.bw.boot.thrift.client.autoconfigure;

import io.github.bw.boot.thrift.client.ThriftClientBeanPostProcessor;
import io.github.bw.boot.thrift.client.config.ThriftClientProperties;
import io.github.bw.boot.thrift.client.context.ThriftClientBeanScanProcessor;
import org.apache.thrift.TServiceClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass(TServiceClient.class)
@Configuration
@EnableConfigurationProperties(ThriftClientProperties.class)
public class ThriftClientAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ThriftClientProperties thriftClientProperties() {
    return new ThriftClientProperties();
  }

  @Bean
  @ConditionalOnMissingBean
  public ThriftClientBeanScanProcessor thriftClientBeanScannerConfigurer() {
    return new ThriftClientBeanScanProcessor();
  }

  @Bean
  @ConditionalOnMissingBean
  public ThriftClientBeanPostProcessor thriftClientBeanPostProcessor() {
    return new ThriftClientBeanPostProcessor();
  }
}
