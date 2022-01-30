package io.github.bw.boot.thrift.client.loadbalancer;

import java.util.List;

@FunctionalInterface
interface ServiceDiscovery {

  List<ServiceInstance> getInstances(String serviceId);

}
