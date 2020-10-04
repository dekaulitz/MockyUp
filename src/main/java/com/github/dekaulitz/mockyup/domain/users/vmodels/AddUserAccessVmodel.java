package com.github.dekaulitz.mockyup.domain.users.vmodels;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddUserAccessVmodel {

  @NotEmpty(message = "Please provide a userId")
  private String userId;
  @NotEmpty(message = "Please provide a access")
  private String access;
}
