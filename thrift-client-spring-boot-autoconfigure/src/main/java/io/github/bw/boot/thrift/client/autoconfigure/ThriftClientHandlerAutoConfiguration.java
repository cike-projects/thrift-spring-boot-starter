package io.github.bw.boot.thrift.client.autoconfigure;

import io.github.bw.boot.thrift.client.config.ThriftClientProperties;
import io.github.bw.boot.thrift.client.context.ThriftClientBeanScanProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TServiceClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass(TServiceClient.class)
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "thrift.client", value = "enabled", matchIfMissing = true)
@Slf4j
@EnableConfigurationProperties(ThriftClientProperties.class)
@ConditionalOnBean(ThriftClientAutoConfiguration.class)
public class ThriftClientHandlerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnBean(ThriftClientProperties.class)
  public ThriftClientBeanScanProcessor thriftClientBeanScannerConfigurer(
      ThriftClientProperties thriftClientProperties) {
    log.info("thriftClientProperties: {}", thriftClientProperties);
    return new ThriftClientBeanScanProcessor();
  }

//  @Bean
//  @ConditionalOnMissingBean
//  @ConditionalOnProperty(prefix = "thrift.client.loadBalance", value = "enabled", matchIfMissing = true)
//  @ConditionalOnClass({DiscoveryClient.class})
//  @ConditionalOnBean({DiscoveryClient.class})
//  @Lazy
//  public CommonCloudServiceDiscovery commonCloudServiceDiscovery(DiscoveryClient discoveryClient) {
//    return new CommonCloudServiceDiscovery(discoveryClient);
//  }
}
