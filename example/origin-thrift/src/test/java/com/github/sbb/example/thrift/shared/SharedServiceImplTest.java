package com.github.sbb.example.thrift.shared;

import com.github.sbb.example.shared.SharedService;
import com.github.sbb.example.shared.SharedStruct;
import java.util.concurrent.TimeUnit;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class SharedServiceImplTest {

  private Thread serverThread;

  @Order(1)
  @Test
  void server() throws TTransportException, InterruptedException {
    //设置服务器端口  TNonblockingServerSocket-非堵塞服务模型
    TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(8899);
    //参数设置
    THsHaServer.Args arg = new THsHaServer.Args(serverSocket).minWorkerThreads(2).maxWorkerThreads(4);
    //处理器
    SharedService.Processor<SharedServiceImpl> processor = new SharedService.Processor<>(new SharedServiceImpl());
    arg.protocolFactory(new TCompactProtocol.Factory());
    arg.transportFactory(new TFramedTransport.Factory());
    arg.processorFactory(new TProcessorFactory(processor));

    serverThread = new Thread(()-> {
      TServer server = new THsHaServer(arg);
      System.out.println("Thrift 服务端启动成功");
      server.serve();
    });

    serverThread.start();
    TimeUnit.SECONDS.sleep(2);
  }

  @Order(3)
  @Test
  void client() throws Exception {
    TTransport transport = new TFramedTransport(new TSocket("localhost", 8899), 600);
    TProtocol protocol = new TCompactProtocol(transport);
    SharedService.Client client = new SharedService.Client(protocol);
    try {
      transport.open();
      SharedStruct struct = client.getStruct(1);
      System.out.println(struct.getValue());
    }catch (Exception ex){
      throw new RuntimeException(ex.getMessage(),ex);
    }finally {
      transport.close();
    }
  }

  @AfterAll
  void close() {
    if (serverThread != null && serverThread.isAlive()) {
      serverThread.interrupt();
    }
  }
}
