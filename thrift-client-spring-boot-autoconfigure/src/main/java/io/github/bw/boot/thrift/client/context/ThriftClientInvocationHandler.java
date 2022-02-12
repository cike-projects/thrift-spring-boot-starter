package io.github.bw.boot.thrift.client.context;

import io.github.bw.boot.thrift.client.ThriftClientHolder;
import io.github.bw.boot.thrift.client.config.ThriftClientProperties.ThriftClientLoadBalance;
import io.github.bw.boot.thrift.client.loadbalancer.FixedInstanceDiscovery;
import io.github.bw.boot.thrift.client.loadbalancer.IRule;
import io.github.bw.boot.thrift.client.loadbalancer.RandomRule;
import io.github.bw.boot.thrift.client.loadbalancer.ServiceDiscovery;
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
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

@Slf4j
public class ThriftClientInvocationHandler implements InvocationHandler {

  private final String serviceId;
  private String serviceName;
  private final Class<?> clientClass;
  private IRule iRule;
  private ApplicationContext applicationContext;

  public void init() {
    ServiceDiscovery discovery = null;

    ThriftClientLoadBalance loadBalance = ThriftClientHolder.getClientProperties().getLoadBalance();

    boolean isEnabledDiscovery = loadBalance != null && loadBalance.isEnabled();

    boolean hasInstances =
        loadBalance != null && loadBalance.getServices() != null && loadBalance.getServices().stream()
            .anyMatch(it -> it.getName().equals(serviceId) && !CollectionUtils.isEmpty(it.getAddress()));

    if (hasInstances) {
      // 配置文件中手动设置了地址
      discovery = new FixedInstanceDiscovery();
    } else if (isEnabledDiscovery) {
      // 配置文件中未手动配置，但开启了服务发现
      try {
        discovery = applicationContext.getBean(ServiceDiscovery.class);
      } catch (Exception e) {
        log.error("Thrift {} 无法使用服务发现", serviceId, e);
        throw e;
      }
    } else {
      // 配置文件中即没有手动配置，也没有开发服务发现
      throw new RuntimeException("Thrift Client " + serviceId + " 配置文件中即没有手动配置，也没有无法服务发现");
    }
    this.iRule = new RandomRule(discovery);
  }

  public ThriftClientInvocationHandler(String serviceId, String serviceName, Class<?> clientClass,
      ApplicationContext applicationContext) {
    this.serviceId = Objects.requireNonNull(serviceId);
    this.serviceName = Objects.requireNonNull(serviceName);
    this.clientClass = Objects.requireNonNull(clientClass);
    this.applicationContext = applicationContext;
    ThriftClientHolder.registerPostProcessor(this::init);
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
