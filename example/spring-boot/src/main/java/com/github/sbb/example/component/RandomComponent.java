package com.github.sbb.example.component;

import com.github.sbb.example.util.RandomStringUtil;
import org.springframework.stereotype.Component;

@Component
public class RandomComponent {

  public String randomString(int count) {
    return RandomStringUtil.random(count);
  }
}
