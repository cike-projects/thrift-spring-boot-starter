package com.github.sbb.boot.thrift.client.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import lombok.Getter;
import lombok.Setter;
import org.apache.thrift.TServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

@Setter
@Getter
public class ThriftClientFactoryBean<T> implements FactoryBean<T>, InitializingBean {

  private static final Logger LOGGER = LoggerFactory.getLogger(ThriftClientFactoryBean.class);

  private String beanName;

  private Class<?> beanClass;

  private String serviceName;

  private String serviceId;

  private Class<?> clientClass;

  private Constructor<? extends TServiceClient> clientConstructor;

  @Override
  @SuppressWarnings("unchecked")
  public T getObject() throws Exception {
    if (beanClass.isInterface()) {
      LOGGER.info("Prepare to generate proxy for {} with JDK", beanClass.getName());
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

  @Override
  public void afterPropertiesSet() throws Exception {
    LOGGER.info("Succeed to instantiate an instance of ThriftClientFactoryBean: {}", this);
  }
}
