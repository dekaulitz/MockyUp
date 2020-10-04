package com.github.dekaulitz.mockyup.domain.mocks.vmodels;

import java.util.Date;
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
public class UserDetailMocks {

  private String id;
  private String username;
  private String access;
  private Date updatedDate;
}
