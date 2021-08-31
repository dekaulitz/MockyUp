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
@ToString(callSuper = true)
@Builder
public class GetProjectContractParam extends PageableParam implements Serializable {

  private String[] ids;
  private String id;
  private String projectId;


  public String toStringSkipNulls() {
    return removeToStringNullValues(toString());
  }
}
