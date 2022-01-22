package com.github.sbb.boot.thrift.client.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ToString
@Setter
@Getter
@ConfigurationProperties("thrift.client")
public class ThriftClientProperties {

  private ThriftClientLoadBalance loadBalance;

  private boolean enabled = true;


}
