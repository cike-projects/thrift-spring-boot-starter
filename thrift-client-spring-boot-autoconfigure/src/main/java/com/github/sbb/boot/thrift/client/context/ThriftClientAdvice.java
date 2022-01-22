package com.github.sbb.boot.thrift.client.context;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

@Slf4j
public class ThriftClientAdvice implements MethodInterceptor {

  public ThriftClientAdvice() {
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    return null;
  }
}
