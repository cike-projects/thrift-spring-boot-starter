package io.github.bw.boot.thrift.client.context;

import io.github.bw.boot.thrift.client.loadbalancer.FixedInstanceDiscovery;
import io.github.bw.boot.thrift.client.loadbalancer.IRule;
import io.github.bw.boot.thrift.client.loadbalancer.RandomRule;
import io.github.bw.boot.thrift.client.loadbalancer.ServiceInstance;
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
  private IRule iRule;

  public ThriftClientInvocationHandler(String serviceId, String serviceName, Class<?> beanClass, Class<?> clientClass) {
    this.serviceId = Objects.requireNonNull(serviceId);
    this.serviceName = Objects.requireNonNull(serviceName);
    this.beanClass = Objects.requireNonNull(beanClass);
    this.clientClass = Objects.requireNonNull(clientClass);
    FixedInstanceDiscovery discovery = new FixedInstanceDiscovery();
    this.iRule = new RandomRule(discovery);
  }

  /**
   * 这里是 Client 接口被调用的真正的处理逻辑
   * <p>
   * 这里可以考虑增强一下功能比如： 1. client 增加池化处理，不必要每次都创建 2. 增加指定地址的负载均衡策略 3. 增加基于注册发现的负载均衡策略
   *
   * @param proxy  代理，这个一直是空的，不用管
   * @param method 被调用的方法
   * @param args   参数
   */
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (isObjectOriginMethod(method)) {
      return invokeObjectOriginMethod(proxy, method, args);
    }

    ServiceInstance serviceInstance = iRule.choose(serviceId);

    TTransport transport = new TFramedTransport(new TSocket(serviceInstance.getHost(), serviceInstance.getPort()));
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

  private Object invokeObjectOriginMethod(Object proxy, Method method, Object[] args) {
    return this.toString();
  }

  private static boolean isObjectOriginMethod(Method method) {
    return method.getName().equals("toString");
  }
}
