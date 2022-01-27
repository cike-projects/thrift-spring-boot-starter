package io.github.bw.boot.thrift.client.context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThriftClientInvocationHandler implements InvocationHandler {

  public ThriftClientInvocationHandler()
      throws Exception {
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    log.info("proxy:{}, method:{}, args:{}", proxy, method, args);
    return null;
  }
}
