package com.github.sbb.example.client;

import com.github.sbb.example.shared.SharedService;
import io.github.bw.boot.thrift.client.ThriftClient;

@ThriftClient(name = "SharedService", serviceId = "spring-boot-thrift-server", fallback = SharedClientFallback.class)
public interface SharedClient extends SharedService.Iface {

}
