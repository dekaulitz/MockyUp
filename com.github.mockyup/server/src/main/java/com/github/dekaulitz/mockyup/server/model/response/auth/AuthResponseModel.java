package com.github.dekaulitz.mockyup.server.model.response.auth;

import com.github.dekaulitz.mockyup.server.model.common.BaseModel;
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
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder(toBuilder = true)
@ToString(callSuper = true)
public class AuthResponseModel extends BaseModel {

  @NotBlank
  private String username;
  @NotBlank
  private String accessToken;
  private Set<Role> access = new HashSet<>();
}
