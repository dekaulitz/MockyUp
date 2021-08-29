package com.github.dekaulitz.mockyup.server.domain.mocks.vmodels;

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
public class CreatorMockVmodel {

  private String userId;
  private String username;
}
