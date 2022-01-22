package com.github.sbb.example.thrift.shared;

import com.github.sbb.example.shared.SharedService;
import com.github.sbb.example.shared.SharedStruct;
import org.apache.thrift.TException;

public class SharedServiceImpl implements SharedService.Iface {

  @Override
  public SharedStruct getStruct(int key) throws TException {
    return new SharedStruct(key, "SharedService" + key);
  }
}
