package com.github.sbb.boot.thrift.server.autoconfigure;

import com.github.sbb.boot.thrift.server.ThriftServer;
import com.github.sbb.boot.thrift.server.ThriftServiceDiscoverer;
import com.github.sbb.boot.thrift.server.server.THsHaThriftServer;
import com.github.sbb.boot.thrift.server.server.TSimpleThriftServer;

public class ThriftServerSelector {

  private final ThriftServerProperties thriftServerProperties;
  private final ThriftServiceDiscoverer thriftServiceDiscoverer;

  public ThriftServerSelector(ThriftServerProperties thriftServerProperties,
      ThriftServiceDiscoverer thriftServiceDiscoverer) {
    this.thriftServerProperties = thriftServerProperties;
    this.thriftServiceDiscoverer = thriftServiceDiscoverer;
  }

  /**
   * THsHaServer  TNonblockingServer  TSimpleServer   TThreadPoolServer TThreadedSelectorServer
   */
  public ThriftServer buildServer() {
    ThriftServer thriftServer = null;
    switch (thriftServerProperties.getServerMode()) {
      case "THsHaServer":
        thriftServer = new THsHaThriftServer(thriftServerProperties.getPort(), thriftServiceDiscoverer);
        break;
      case "TNonblockingServer":
        break;
      case "TSimpleServer":
        thriftServer = new TSimpleThriftServer(thriftServerProperties.getPort(), thriftServiceDiscoverer);
        break;
      case "TThreadPoolServer":
      case "TThreadedSelectorServer":
        break;
    }
    return thriftServer;
  }
}
