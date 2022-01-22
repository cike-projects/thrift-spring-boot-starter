package com.github.sbb.boot.thrift.client;

import org.apache.thrift.TServiceClient;

public interface ThriftClientAware<T extends TServiceClient> {

  T client();
}
