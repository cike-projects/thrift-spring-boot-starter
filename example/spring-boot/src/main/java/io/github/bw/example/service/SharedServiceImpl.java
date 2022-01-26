package io.github.bw.example.service;

import io.github.bw.boot.thrift.server.ThriftService;
import io.github.bw.example.component.RandomComponent;
import io.github.bw.example.shared.SharedService;
import io.github.bw.example.shared.SharedStruct;
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
