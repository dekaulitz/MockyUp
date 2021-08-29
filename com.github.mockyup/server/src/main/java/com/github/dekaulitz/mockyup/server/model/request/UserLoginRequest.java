package com.github.dekaulitz.mockyup.server.model.request;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
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
public class UserLoginRequest implements Serializable {

  @NotNull
  private String email;

  @NotNull
  private String password;

  private boolean rememberMe;
}
