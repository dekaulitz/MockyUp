package com.github.dekaulitz.mockyup.domain.mocks.vmodels;

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
public class UserMockVmodel {

  private String userId;
  private String access;
}
