package com.github.sbb.example.controller;

import com.github.sbb.example.client.SharedClient;
import com.github.sbb.example.shared.SharedStruct;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@RestController
@RequestMapping("SharedClient")
public class SharedController {

  @Autowired
  private SharedClient sharedClient;

  @GetMapping("s")
  public SharedStruct getStruct(int key) throws TException {
    return sharedClient.getStruct(key);
  }

}
