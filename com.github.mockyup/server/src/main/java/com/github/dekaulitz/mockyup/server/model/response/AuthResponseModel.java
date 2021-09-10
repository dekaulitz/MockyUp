package com.github.dekaulitz.mockyup.server.model.response;

import com.github.dekaulitz.mockyup.server.model.constants.Role;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class AuthResponseModel implements Serializable {

  @NotBlank
  private String username;
  @NotBlank
  private String accessToken;
  private Set<Role> access = new HashSet<>();
}
