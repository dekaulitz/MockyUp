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
public class GetProjectParam extends PageableParam implements Serializable {

  private String id;
  private String[] ids;
  private String projectName;
  private String tag;

  public String toStringSkipNulls() {
    return removeToStringNullValues(toString());
  }
}
