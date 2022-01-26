package io.github.bw.boot.thrift.server;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

public abstract class ThriftServer {

  public ThriftServer(int port, ThriftServiceDiscoverer serviceDiscoverer) {
    this.port = port;
    this.serviceDiscoverer = serviceDiscoverer;
  }

  private final ThriftServiceDiscoverer serviceDiscoverer;

  private int port;

  public abstract void start() throws Exception;

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public List<ThriftServiceDefinition> getImmutableServices() {
    return serviceDiscoverer.findThriftServices();
  }

  @SneakyThrows
  public TMultiplexedProcessor build(List<ThriftServiceDefinition> definitions) {
    if (CollectionUtils.isEmpty(definitions)) {
      return null;
    }
    TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();

    for (ThriftServiceDefinition definition : definitions) {
      Object handler = definition.getHandler();
      Class<?> ifaceClass = Stream.of(ClassUtils.getAllInterfaces(handler))
          .filter(clazz -> clazz.getName().endsWith("$Iface"))
          .filter(iFace -> iFace.getDeclaringClass() != null)
          .findFirst()
          .orElseThrow(() -> new IllegalStateException("No thrift IFace found on implementation"));

      Class<TProcessor> processorClass = Stream.of(ifaceClass.getDeclaringClass().getDeclaredClasses())
          .filter(clazz -> clazz.getName().endsWith("$Processor"))
          .filter(TProcessor.class::isAssignableFrom)
          .findFirst()
          .map(processor -> (Class<TProcessor>) processor)
          .orElseThrow(() -> new IllegalStateException("No thrift IFace found on implementation"));

      Constructor<TProcessor> processorConstructor = processorClass.getConstructor(ifaceClass);

      TProcessor singleProcessor = BeanUtils.instantiateClass(processorConstructor, handler);

      multiplexedProcessor.registerProcessor(definition.getServiceName(), singleProcessor);
    }

    return multiplexedProcessor;
  }

  public abstract void shutdown();

  public abstract void shutdownNow();

  public abstract boolean isRunning();
}
