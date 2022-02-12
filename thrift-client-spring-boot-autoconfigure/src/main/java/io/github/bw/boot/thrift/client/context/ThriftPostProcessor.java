package io.github.bw.boot.thrift.client.context;

@FunctionalInterface
public interface ThriftPostProcessor {

  void execute();

}
