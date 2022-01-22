package com.github.sbb.boot.thrift.server.server;

import com.github.sbb.boot.thrift.server.ThriftServer;
import com.github.sbb.boot.thrift.server.ThriftServiceDiscoverer;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;

@Slf4j
public class TSimpleThriftServer extends ThriftServer {

  private TServer server;

  public TSimpleThriftServer(int port, ThriftServiceDiscoverer serviceDiscoverer) {
    super(port, serviceDiscoverer);
  }

  @Override
  public void start() throws Exception {
    TServerSocket serverSocket = new TServerSocket(getPort());
    TServer.Args serverParams = new TServer.Args(serverSocket);
    serverParams.protocolFactory(new TBinaryProtocol.Factory());
    server = new TSimpleServer(serverParams);
    Thread serverThread = new Thread(() -> {
      server.serve();
    });
    serverThread.setDaemon(true);
    serverThread.start();
    log.info("Thrift Starting [\"TSimpleServer-{}\"]", getPort());
  }

  @Override
  public void shutdown() {
    if (server != null) {
      server.setShouldStop(true);
    }
  }

  @Override
  public void shutdownNow() {
    if (server != null) {
      server.stop();
    }
  }

  @Override
  public boolean isRunning() {
    return server != null && server.isServing();
  }

}
