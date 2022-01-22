package com.github.sbb.boot.thrift.client.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ThriftClientLoadBalance {

  private boolean enabled = false;
}
