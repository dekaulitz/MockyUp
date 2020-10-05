package com.github.dekaulitz.mockyup.infrastructure.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class JwtProperties {

  @Value("${mock.auth.secret}")
  private String secret;
  @Value("${mock.auth.refresh}")
  private Long refreshTime;
  @Value("${mock.auth.expired}")
  private Long expiredTime;

}
