package io.github.bw.boot.thrift.client.loadbalancer;

import io.github.bw.boot.thrift.client.ThriftClientHolder;
import io.github.bw.boot.thrift.client.config.ThriftClientProperties;
import java.util.List;
import java.util.stream.Collectors;

public class FixedInstanceDiscovery implements ServiceDiscovery {

  @Override
  public List<ServiceInstance> getInstances(String serviceId) {
    ThriftClientProperties clientProperties = ThriftClientHolder.getClientProperties();

    return clientProperties.getLoadBalance().getServices().stream()
        .filter(it -> it.getName().equals(serviceId))
        .map(it -> {
          String targetHost = it.getAddress().get(0);
          String[] hostInfo = targetHost.split(":");
          return new DefaultServiceInstance(hostInfo[0], Integer.parseInt(hostInfo[1]));
        }).collect(Collectors.toList());
  }
}
