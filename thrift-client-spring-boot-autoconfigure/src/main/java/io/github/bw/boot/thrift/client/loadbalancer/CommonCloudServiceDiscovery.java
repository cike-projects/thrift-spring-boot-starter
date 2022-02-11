package io.github.bw.boot.thrift.client.loadbalancer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;

public class CommonCloudServiceDiscovery implements ServiceDiscovery {

  private DiscoveryClient discoveryClient;

  public CommonCloudServiceDiscovery(DiscoveryClient discoveryClient) {
    this.discoveryClient = discoveryClient;
  }

  @Override
  public List<ServiceInstance> getInstances(String serviceId) {
    List<org.springframework.cloud.client.ServiceInstance> instances = discoveryClient.getInstances(serviceId);

    if (CollectionUtils.isEmpty(instances)) {
      return new ArrayList<>();
    }

    return instances.stream().map(it -> new DefaultServiceInstance(it.getHost(), it.getPort()))
        .collect(Collectors.toList());
  }
}
