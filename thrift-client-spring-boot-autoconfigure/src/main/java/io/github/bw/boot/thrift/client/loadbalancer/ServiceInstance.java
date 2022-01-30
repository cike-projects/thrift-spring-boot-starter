package io.github.bw.boot.thrift.client.loadbalancer;

public interface ServiceInstance {

  String getHost();

  int getPort();

}
