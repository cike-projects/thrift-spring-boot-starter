package io.github.bw.boot.thrift.client.loadbalancer;

public interface IRule {

  ServiceInstance choose(String key);

  void setServiceDiscovery(ServiceDiscovery discovery);
}
