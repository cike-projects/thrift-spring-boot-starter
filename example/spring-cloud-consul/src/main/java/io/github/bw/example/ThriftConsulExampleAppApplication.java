package io.github.bw.example;

import io.github.bw.boot.thrift.client.annotation.ThriftClientScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@ThriftClientScan("io.github.bw.example.client")
@EnableDiscoveryClient
@SpringBootApplication
public class ThriftConsulExampleAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(ThriftConsulExampleAppApplication.class, args);
  }
}
