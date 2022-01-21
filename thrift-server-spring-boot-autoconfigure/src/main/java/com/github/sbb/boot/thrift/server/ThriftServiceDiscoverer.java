package com.github.sbb.boot.thrift.server;

import java.util.Collection;
import java.util.List;

@FunctionalInterface
public interface ThriftServiceDiscoverer {

  List<ThriftServiceDefinition> findThriftServices();

}
