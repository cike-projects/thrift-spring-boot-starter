package io.github.bw.boot.thrift.client.context.demo;

import io.github.bw.boot.thrift.client.ThriftClient;
import io.github.bw.example.shared.SharedService;

@ThriftClient(name = "SharedService", serviceId = "spring-boot-thrift-server", fallback = SharedClientFallback.class)
public interface SharedClient extends SharedService.Iface {

}
