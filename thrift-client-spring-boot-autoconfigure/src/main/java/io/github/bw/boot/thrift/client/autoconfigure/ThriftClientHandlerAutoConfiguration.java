package io.github.bw.boot.thrift.client.autoconfigure;

import io.github.bw.boot.thrift.client.config.ThriftClientProperties;
import io.github.bw.boot.thrift.client.loadbalancer.CommonCloudServiceDiscovery;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TServiceClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@ConditionalOnClass(value = TServiceClient.class, name = "org.springframework.cloud.client.discovery.DiscoveryClient")
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "thrift.client", value = "enabled", matchIfMissing = true)
@Slf4j
@EnableConfigurationProperties(ThriftClientProperties.class)
@ConditionalOnBean(ThriftClientAutoConfiguration.class)
public class ThriftClientHandlerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnProperty(prefix = "thrift.client.loadBalance", value = "enabled", matchIfMissing = true)
  @ConditionalOnBean(type = "org.springframework.cloud.client.discovery.DiscoveryClient")
  @Lazy
  public CommonCloudServiceDiscovery commonCloudServiceDiscovery(DiscoveryClient discoveryClient) {
    return new CommonCloudServiceDiscovery(discoveryClient);
  }
}
