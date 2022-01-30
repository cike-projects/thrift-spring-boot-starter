package io.github.bw.boot.thrift.client.loadbalancer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.util.CollectionUtils;

public class RandomRule extends AbstractLoadBalancerRule {

  private static final byte MAX_RETRY_INTERVAL = 3;

  public RandomRule() {
  }

  public RandomRule(ServiceDiscovery discovery) {
    super();
    super.discovery = discovery;
  }


  @Override
  public ServiceInstance choose(String serviceId) {
    if (discovery == null) {
      return null;
    }

    for (byte retryInterval = 0; retryInterval < MAX_RETRY_INTERVAL; ++retryInterval) {
      if (Thread.interrupted()) {
        return null;
      }

      List<ServiceInstance> instances = discovery.getInstances(serviceId);
      if (CollectionUtils.isEmpty(instances)) {
        Thread.yield();
        continue;
      }

      int index = this.chooseRandomInt(instances.size());
      ServiceInstance server = instances.get(index);
      if (server == null) {
        Thread.yield();
      } else {
        return server;
      }
    }
    return null;
  }

  protected int chooseRandomInt(int serverCount) {
    return ThreadLocalRandom.current().nextInt(serverCount);
  }

}
