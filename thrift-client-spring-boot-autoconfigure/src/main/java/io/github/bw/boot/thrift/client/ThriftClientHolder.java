package io.github.bw.boot.thrift.client;

import io.github.bw.boot.thrift.client.config.ThriftClientProperties;

public class ThriftClientHolder {

  private static ThriftClientProperties clientProperties;

  public static ThriftClientProperties getClientProperties() {
    return clientProperties;
  }

  public static void setClientProperties(ThriftClientProperties clientProperties) {
    ThriftClientHolder.clientProperties = clientProperties;
  }
}
