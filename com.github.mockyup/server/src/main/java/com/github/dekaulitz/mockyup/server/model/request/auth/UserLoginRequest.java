package com.github.dekaulitz.mockyup.server.model.request.auth;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString(callSuper = true)
public class UserLoginRequest implements Serializable {

  @NotNull
  private String usernameOrEmail;

  @NotNull
  private String password;

  private boolean rememberMe;
}
