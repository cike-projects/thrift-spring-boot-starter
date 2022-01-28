package io.github.bw.boot.thrift.client.context;

import io.github.bw.boot.thrift.client.ThriftClientHolder;
import io.github.bw.boot.thrift.client.config.ServiceNode;
import io.github.bw.boot.thrift.client.config.ThriftClientProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.util.ReflectionUtils;

@Slf4j
public class ThriftClientInvocationHandler implements InvocationHandler {

  private final String serviceId;
  private String serviceName;
  private Class<?> beanClass;
  private final Class<?> clientClass;

  public ThriftClientInvocationHandler(String serviceId, String serviceName, Class<?> beanClass, Class<?> clientClass) {
    this.serviceId = Objects.requireNonNull(serviceId);
    this.serviceName = Objects.requireNonNull(serviceName);
    this.beanClass = Objects.requireNonNull(beanClass);
    this.clientClass = Objects.requireNonNull(clientClass);
  }


  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    ThriftClientProperties clientProperties = ThriftClientHolder.getClientProperties();

    ServiceNode serviceNode = clientProperties.getLoadBalance().getServices().stream()
        .filter(it -> it.getName().equals(serviceId))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No find available server for " + serviceId));

    String targetHost = serviceNode.getAddress().get(0);
    String[] hostInfo = targetHost.split(":");
    TTransport transport = new TFramedTransport(new TSocket(hostInfo[0], Integer.parseInt(hostInfo[1])));
    TProtocol protocol = new TCompactProtocol(transport);

    TMultiplexedProtocol targetMultiplexedProtocol = new TMultiplexedProtocol(protocol, serviceName);
    Constructor<?> constructor = ReflectionUtils.accessibleConstructor(clientClass, TProtocol.class);
    Object clientInstance = constructor.newInstance(targetMultiplexedProtocol);

    Method clientMethod = ReflectionUtils.findMethod(clientClass, method.getName(), method.getParameterTypes());
    Objects.requireNonNull(clientMethod);
    transport.open();
    log.info("proxy:{}, method:{}, args:{}", proxy, method, args);
    return ReflectionUtils.invokeMethod(clientMethod, clientInstance, args);
  }
}
