package com.github.dekaulitz.mockyup.domain.mocks.vmodels;

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
public class DtoMockupDetailUpdatedByVmodel {

  private String userId;
  private String username;
}
