package io.github.bw.boot.thrift.client;

import io.github.bw.boot.thrift.client.config.ThriftClientProperties;
import io.github.bw.boot.thrift.client.context.ThriftPostProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ThriftClientHolder {

  private static ThriftClientProperties clientProperties;

  private static final List<ThriftPostProcessor> registerPostProcessor = new ArrayList<>();

  public static ThriftClientProperties getClientProperties() {
    return clientProperties;
  }

  public static void setClientProperties(ThriftClientProperties clientProperties) {
    ThriftClientHolder.clientProperties = clientProperties;
  }

  public static void registerPostProcessor(ThriftPostProcessor processor) {
    registerPostProcessor.add(Objects.requireNonNull(processor));
  }

  public static List<ThriftPostProcessor> getNeedPostProcessor() {
    return registerPostProcessor;
  }

}
