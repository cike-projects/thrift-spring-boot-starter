package io.github.bw.boot.thrift.client.context;

import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

@Setter
@Getter
public class ThriftClientFactoryBean<T> implements FactoryBean<T> {

  private static final Logger logger = LoggerFactory.getLogger(ThriftClientFactoryBean.class);

  private Class<?> beanClass;
  private String serviceId;
  private String serviceName;

  public ThriftClientFactoryBean(String serviceId, String serviceName, Class<?> beanClass) {
    this.beanClass = beanClass;
    this.serviceId = serviceId;
    this.serviceName = serviceName;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T getObject() {
    if (beanClass.isInterface()) {
      logger.info("Prepare to generate proxy for {} with JDK", beanClass.getName());
      // 获取到对应 client 的 class
      ThriftClientInvocationHandler invocationHandler = new ThriftClientInvocationHandler(serviceId, serviceName,
          beanClass, getClientClass(beanClass));
      return (T) Proxy.newProxyInstance(beanClass.getClassLoader(), new Class<?>[]{beanClass}, invocationHandler);
    }
    return null;
  }

  /**
   * 通过实现的 Iface 接口获取到对应的 client class, 这种写法有点诡异
   *
   * @param beanClass 标注有 @ThriftClient 的接口
   */
  @SneakyThrows
  private Class<?> getClientClass(Class<?> beanClass) {
    Type iFaceType = Stream.of(beanClass.getGenericInterfaces())
        .filter(clazz -> ((Class<?>) clazz).getName().endsWith("$Iface"))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No thrift IFace found on implementation"));

    return Class.forName(iFaceType.getTypeName().replace("$Iface", "$Client"));
  }

  @Override
  public Class<?> getObjectType() {
    return beanClass;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

}
