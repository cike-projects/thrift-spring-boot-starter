package io.github.bw.boot.thrift.server;

import java.util.List;

@FunctionalInterface
public interface ThriftServiceDiscoverer {

  List<ThriftServiceDefinition> findThriftServices();

}
