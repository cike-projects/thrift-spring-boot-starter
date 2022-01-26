package io.github.bw.example.thrift.shared;

import io.github.bw.example.shared.SharedService;
import io.github.bw.example.shared.SharedStruct;
import io.github.bw.example.tutorial.Calculator;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
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

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TMultiplexedProcessorTest {

  private Thread serverThread;

  private static final int PORT = 10109;

  @Order(1)
  @Test
  void server() throws TTransportException, InterruptedException {
    //设置服务器端口  TNonblockingServerSocket-非堵塞服务模型
    TNonblockingServerSocket serverSocket = new TNonblockingServerSocket(PORT);

    //处理器
    SharedService.Processor<SharedServiceImpl> sharedProcessor = new SharedService.Processor<>(new SharedServiceImpl());
    Calculator.Processor<CalculatorServiceImpl> calculatorProcessor = new Calculator.Processor<>(
        new CalculatorServiceImpl());

    TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();
    multiplexedProcessor.registerProcessor("SharedService", sharedProcessor);
    multiplexedProcessor.registerProcessor("calculatorProcessor", calculatorProcessor);

    THsHaServer.Args arg = new THsHaServer.Args(serverSocket).minWorkerThreads(2).maxWorkerThreads(4);

    arg.protocolFactory(new TCompactProtocol.Factory())
        .transportFactory(new TFramedTransport.Factory())
        .processorFactory(new TProcessorFactory(multiplexedProcessor));

    serverThread = new Thread(() -> {
      TServer server = new THsHaServer(arg);
      System.out.println("Thrift 服务端启动成功");
      try {
        server.serve();
      } catch (Exception e) {
        log.error("start exception", e);
      }
    });

    serverThread.start();
    TimeUnit.SECONDS.sleep(2);
  }

  @Order(3)
  @Test
  void client() throws Exception {
    TTransport transport = new TFramedTransport(new TSocket("localhost", PORT));
    TProtocol protocol = new TCompactProtocol(transport);

    TMultiplexedProtocol sharedProtocol = new TMultiplexedProtocol(protocol, "SharedService");
    SharedService.Client client = new SharedService.Client(sharedProtocol);

    try {
      transport.open();
      SharedStruct struct = client.getStruct(1);
      System.out.println(struct.getValue());
    } catch (Exception ex) {
      throw new RuntimeException(ex.getMessage(), ex);
    } finally {
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
