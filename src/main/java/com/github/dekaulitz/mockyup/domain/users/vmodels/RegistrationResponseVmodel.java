package com.github.dekaulitz.mockyup.domain.users.vmodels;

import java.util.List;
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
public class RegistrationResponseVmodel {

  private String id;
  private String username;
  private List<String> accessList;
}
