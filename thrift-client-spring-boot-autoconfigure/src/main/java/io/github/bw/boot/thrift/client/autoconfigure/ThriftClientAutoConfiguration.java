package io.github.bw.boot.thrift.client.autoconfigure;

import io.github.bw.boot.thrift.client.ThriftClientHolder;
import io.github.bw.boot.thrift.client.config.ThriftClientProperties;
import org.apache.thrift.TServiceClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(TServiceClient.class)
@EnableConfigurationProperties(ThriftClientProperties.class)
@ConditionalOnProperty(prefix = "thrift.client", value = "enabled", matchIfMissing = true)
public class ThriftClientAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ThriftClientProperties thriftClientProperties() {
    ThriftClientProperties thriftClientProperties = new ThriftClientProperties();
    ThriftClientHolder.setClientProperties(thriftClientProperties);
    return thriftClientProperties;
  }
}
