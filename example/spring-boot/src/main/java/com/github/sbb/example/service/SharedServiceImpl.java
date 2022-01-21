package com.github.sbb.example.service;

import com.github.sbb.boot.thrift.server.ThriftService;
import com.github.sbb.example.component.RandomComponent;
import com.github.sbb.example.shared.SharedService;
import com.github.sbb.example.shared.SharedStruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ThriftService
@Component
public class SharedServiceImpl implements SharedService.Iface {

  @Autowired
  private RandomComponent randomComponent;

  @Override
  public SharedStruct getStruct(int key) {
    return new SharedStruct(key, randomComponent.randomString(key));
  }
}
