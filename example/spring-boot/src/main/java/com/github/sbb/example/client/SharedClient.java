package com.github.sbb.example.client;

import com.github.sbb.boot.thrift.client.ThriftClient;
import com.github.sbb.example.shared.SharedService;

@ThriftClient(name = "SharedService", serviceId = "spring-boot-thrift-server")
public interface SharedClient extends SharedService.Iface {

}
