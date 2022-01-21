package com.github.sbb.boot.thrift.server;

import java.util.List;

public abstract class ThriftServer {

  public ThriftServer(int port, ThriftServiceDiscoverer serviceDiscoverer) {
    this.port = port;
    this.serviceDiscoverer = serviceDiscoverer;
  }

  private final ThriftServiceDiscoverer serviceDiscoverer;

  private int port;

  public abstract void start() throws Exception;

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public List<ThriftServiceDefinition> getImmutableServices() {
    return serviceDiscoverer.findThriftServices();
  }

  public abstract void shutdown();

  public abstract void shutdownNow();

  public abstract boolean isRunning();
}
