package com.github.dekaulitz.mockyup.server.model.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString(callSuper = true)
public class GetUserLogLoginParam extends PageableParam {

  private String jti;
  private String userId;
}
