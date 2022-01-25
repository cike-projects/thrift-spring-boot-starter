package io.github.bw.boot.thrift.client.config;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ServiceNode {

  private String name;
  private List<String> address;
}
