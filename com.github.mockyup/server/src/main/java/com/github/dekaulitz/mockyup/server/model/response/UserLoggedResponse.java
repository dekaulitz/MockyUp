package com.github.dekaulitz.mockyup.server.model.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class UserLoggedResponse implements Serializable {

  private String id;
  private String token;
  private String username;
  private String email;
}
