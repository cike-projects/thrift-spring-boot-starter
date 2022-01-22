package com.github.sbb.example.service;

import com.github.sbb.boot.thrift.server.ThriftService;
import com.github.sbb.example.component.RandomComponent;
import com.github.sbb.example.shared.SharedService;
import com.github.sbb.example.shared.SharedStruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@RequestMapping("Shared")
@ThriftService("SharedService")
public class SharedServiceImpl implements SharedService.Iface {

  @Autowired
  private RandomComponent randomComponent;

  @GetMapping("getStruct")
  @Override
  public SharedStruct getStruct(int key) {
    return new SharedStruct(key, randomComponent.randomString(key));
  }
}
