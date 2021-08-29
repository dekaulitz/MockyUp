package com.github.dekaulitz.mockyup.server.model.param;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetMockUpParam extends PageableParam implements Serializable {

  private String tittle;
  private String[] ids;
  private String id;

}
