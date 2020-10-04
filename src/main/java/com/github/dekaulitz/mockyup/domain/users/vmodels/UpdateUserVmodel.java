package com.github.dekaulitz.mockyup.domain.users.vmodels;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserVmodel {

  @NotEmpty(message = "Please provide a username")
  private String username;
  private String password;
  private List<String> accessList;
}
