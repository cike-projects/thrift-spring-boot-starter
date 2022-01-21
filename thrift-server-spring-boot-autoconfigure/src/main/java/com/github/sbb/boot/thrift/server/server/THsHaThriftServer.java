package com.github.sbb.boot.thrift.server.server;

import com.github.sbb.boot.thrift.server.ThriftServer;
import com.github.sbb.boot.thrift.server.ThriftServiceDefinition;
import com.github.sbb.boot.thrift.server.ThriftServiceDiscoverer;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

@Slf4j
public class THsHaThriftServer extends ThriftServer {

  private TServer server;

  public THsHaThriftServer(int port, ThriftServiceDiscoverer serviceDiscoverer) {
    super(port, serviceDiscoverer);
  }

  @Override
  public void start() throws TTransportException {
    TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(getPort());
    org.apache.thrift.server.THsHaServer.Args arg = new org.apache.thrift.server.THsHaServer.Args(
        serverSocket).minWorkerThreads(2).maxWorkerThreads(4);

    TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();

    for (ThriftServiceDefinition serviceDefinition : getImmutableServices()) {
      // multiplexedProcessor.registerProcessor();
    }

    arg.protocolFactory(new TCompactProtocol.Factory());
    arg.transportFactory(new TFramedTransport.Factory());
    arg.processorFactory(new TProcessorFactory(multiplexedProcessor));
    server = new org.apache.thrift.server.THsHaServer(arg);

    Thread serverThread = new Thread(() -> {
      server.serve();
    });
    serverThread.setDaemon(true);
    serverThread.start();
    log.info("Thrift 服务端启动成功");
  }

  @Override
  public void shutdown() {
    server.setShouldStop(true);
  }

  @Override
  public void shutdownNow() {
    server.stop();
  }

  @Override
  public boolean isRunning() {
    return server != null && server.isServing();
  }
}
