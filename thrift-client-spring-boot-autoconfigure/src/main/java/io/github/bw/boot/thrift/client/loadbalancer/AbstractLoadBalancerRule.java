package io.github.bw.boot.thrift.client.loadbalancer;

public abstract class AbstractLoadBalancerRule implements IRule {

  protected ServiceDiscovery discovery;

  @Override
  public void setServiceDiscovery(ServiceDiscovery discovery) {
    this.discovery = discovery;
  }
}
