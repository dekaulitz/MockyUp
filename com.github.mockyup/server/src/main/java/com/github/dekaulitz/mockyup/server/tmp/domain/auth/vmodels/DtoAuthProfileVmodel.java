package com.github.dekaulitz.mockyup.server.tmp.domain.auth.vmodels;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

public class DtoAuthProfileVmodel {

  private String id;
  private String username;
  private String token;
  private List<String> accessMenus;
}
