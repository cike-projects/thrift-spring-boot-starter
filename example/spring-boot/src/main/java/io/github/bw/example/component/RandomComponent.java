package io.github.bw.example.component;

import io.github.bw.example.util.RandomStringUtil;
import org.springframework.stereotype.Component;

@Component
public class RandomComponent {

  public String randomString(int count) {
    return RandomStringUtil.random(count);
  }
}
