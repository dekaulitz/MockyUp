package com.github.dekaulitz.mockyup.domain.mocks.vmodels;

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
public class DtoMockUserLookupVmodel {

  private String id;
  private List<UserDetailMocks> users;
}

