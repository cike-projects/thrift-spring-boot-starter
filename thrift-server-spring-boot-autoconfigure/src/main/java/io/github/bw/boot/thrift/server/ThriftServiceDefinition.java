package io.github.bw.boot.thrift.server;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ThriftServiceDefinition {

  private String serviceName;
  private Object handler;

  public ThriftServiceDefinition(String serviceName, Object handler) {
    this.serviceName = serviceName;
    this.handler = handler;
  }
}
