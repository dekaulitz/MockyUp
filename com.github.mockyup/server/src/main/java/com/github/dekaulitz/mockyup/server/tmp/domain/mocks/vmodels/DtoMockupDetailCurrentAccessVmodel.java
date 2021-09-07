package com.github.dekaulitz.mockyup.server.tmp.domain.mocks.vmodels;

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
public class DtoMockupDetailCurrentAccessVmodel {

  private String username;
  private String access;
}
