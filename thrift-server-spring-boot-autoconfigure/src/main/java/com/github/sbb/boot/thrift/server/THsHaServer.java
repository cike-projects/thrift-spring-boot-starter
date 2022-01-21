package com.github.sbb.boot.thrift.server;

import java.util.concurrent.TimeUnit;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

public class THsHaServer extends Server {

  @Override
  public Server start() throws TTransportException {
    //设置服务器端口  TNonblockingServerSocket-非堵塞服务模型
    TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(8899);
    //参数设置
    org.apache.thrift.server.THsHaServer.Args arg = new org.apache.thrift.server.THsHaServer.Args(
        serverSocket).minWorkerThreads(2).maxWorkerThreads(4);

    TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();

    for (ThriftServiceDefinition serviceDefinition : getImmutableServices()) {
      // multiplexedProcessor.registerProcessor();

    }




    //处理器
    arg.protocolFactory(new TCompactProtocol.Factory());
    arg.transportFactory(new TFramedTransport.Factory());
    arg.processorFactory(new TProcessorFactory(multiplexedProcessor));

    Thread serverThread = new Thread(() -> {
      TServer server = new org.apache.thrift.server.THsHaServer(arg);
      System.out.println("Thrift 服务端启动成功");
      server.serve();
    });

    serverThread.start();
  }

  @Override
  public Server shutdown() {
    return null;
  }

  @Override
  public Server shutdownNow() {
    return null;
  }

  @Override
  public boolean isShutdown() {
    return false;
  }

  @Override
  public boolean isTerminated() {
    return false;
  }

  @Override
  public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    return false;
  }

  @Override
  public void awaitTermination() throws InterruptedException {

  }
}
