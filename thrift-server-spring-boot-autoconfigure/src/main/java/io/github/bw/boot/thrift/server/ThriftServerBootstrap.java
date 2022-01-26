package io.github.bw.boot.thrift.server;

import io.github.bw.boot.thrift.server.autoconfigure.ThriftServerSelector;
import lombok.SneakyThrows;
import org.springframework.context.SmartLifecycle;

public class ThriftServerBootstrap implements SmartLifecycle {

  private final ThriftServer thriftServer;

  public ThriftServerBootstrap(ThriftServerSelector thriftServerSelector) {
    this.thriftServer = thriftServerSelector.buildServer();
  }

  @SneakyThrows
  @Override
  public void start() {
    thriftServer.start();
  }

  @Override
  public void stop() {
    thriftServer.shutdownNow();
  }

  @Override
  public boolean isRunning() {
    return thriftServer.isRunning();
  }
}
