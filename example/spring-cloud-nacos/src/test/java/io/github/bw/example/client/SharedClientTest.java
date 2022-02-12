package io.github.bw.example.client;

import io.github.bw.example.shared.SharedStruct;
import org.apache.thrift.TException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DisplayName("SharedClientTest")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SharedClientTest {

  @Autowired
  private SharedClient sharedClient;

  @Test
  void fire() throws TException {
    SharedStruct result = sharedClient.getStruct(1);
    Assertions.assertNotNull(result);
    Assertions.assertEquals(1, result.getKey());
  }
}