package io.github.bw.example;

import io.github.bw.boot.thrift.client.annotation.ThriftClientScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@ThriftClientScan("io.github.bw.example.client")
@SpringBootApplication
public class ThriftExampleAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(ThriftExampleAppApplication.class, args);
  }
}
