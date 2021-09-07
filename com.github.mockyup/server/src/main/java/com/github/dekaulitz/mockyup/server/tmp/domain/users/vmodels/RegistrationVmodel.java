package com.github.dekaulitz.mockyup.server.tmp.domain.users.vmodels;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RegistrationVmodel {

  @NotEmpty(message = "Please provide a username")
  private String username;
  @NotEmpty(message = "Please provide a password")
  private String password;
  private List<String> accessList;
}
