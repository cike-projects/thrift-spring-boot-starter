package com.github.sbb.boot.thrift.server.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ToString
@Setter
@Getter
@ConfigurationProperties("thrift.server")
public class ThriftServerProperties {

  public static final String THRIFT_CLOUD_PORT = "THRIFT_CLOUD_PORT";

  private int port = 10109;
  private String serverMode = "THsHaServer";

}
