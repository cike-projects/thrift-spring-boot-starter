package io.github.bw.boot.thrift.client.loadbalancer;

public class DefaultServiceInstance implements ServiceInstance {

  private final String host;

  private final int port;

  public DefaultServiceInstance(String host, int port) {
    this.host = host;
    this.port = port;
  }

  @Override
  public String getHost() {
    return host;
  }

  @Override
  public int getPort() {
    return port;
  }
}
