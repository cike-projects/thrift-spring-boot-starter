package com.github.sbb.boot.thrift.server;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Server {

  private ThriftServiceDiscoverer serviceDiscoverer;

  private int port = -1;

  public abstract Server start() throws Exception;

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public List<ThriftServiceDefinition> getImmutableServices() {
    return serviceDiscoverer.findThriftServices();
  }

  public abstract Server shutdown();

  public abstract Server shutdownNow();

  public abstract boolean isShutdown();

  public abstract boolean isTerminated();

  public abstract boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

  public abstract void awaitTermination() throws InterruptedException;

}
