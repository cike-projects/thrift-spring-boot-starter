package io.github.bw.example.thrift.shared;

import io.github.bw.example.shared.SharedService;
import io.github.bw.example.shared.SharedStruct;
import org.apache.thrift.TException;

public class SharedServiceImpl implements SharedService.Iface {

  @Override
  public SharedStruct getStruct(int key) throws TException {
    return new SharedStruct(key, "SharedService" + key);
  }
}
