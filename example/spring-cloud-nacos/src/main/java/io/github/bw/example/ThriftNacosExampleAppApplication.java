package io.github.bw.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ThriftNacosExampleAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(ThriftNacosExampleAppApplication.class, args);
  }
}
