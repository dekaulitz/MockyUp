package com.github.dekaulitz.mockyup.server.model.param;

import java.io.Serializable;
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
public class GetUserParam extends PageableParam implements Serializable {

  private String email;
  private String username;

  public String toStringSkipNulls() {
    return removeToStringNullValues(toString());
  }
}
