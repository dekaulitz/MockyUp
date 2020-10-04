package com.github.dekaulitz.mockyup.domain.auth.vmodels;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DoAuthVmodel {

  @NotEmpty(message = "Please provide a username")
  private String username;
  @NotEmpty(message = "Please provide a password")
  private String password;
}
