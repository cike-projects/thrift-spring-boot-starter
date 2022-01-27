package io.github.bw.boot.thrift.client.context;

import java.lang.reflect.Proxy;
import lombok.Getter;
import lombok.Setter;
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
  public T getObject() throws Exception {
    if (beanClass.isInterface()) {
      logger.info("Prepare to generate proxy for {} with JDK", beanClass.getName());
      ThriftClientInvocationHandler invocationHandler = new ThriftClientInvocationHandler();
      return (T) Proxy.newProxyInstance(beanClass.getClassLoader(), new Class<?>[]{beanClass}, invocationHandler);
    }
    return null;
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
