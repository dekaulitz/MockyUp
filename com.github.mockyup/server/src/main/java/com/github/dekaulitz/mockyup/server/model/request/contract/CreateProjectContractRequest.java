package com.github.dekaulitz.mockyup.server.model.request.contract;

import com.github.dekaulitz.mockyup.server.model.common.BaseModel;
import java.util.LinkedHashMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder(toBuilder = true)
@ToString(callSuper = true)
public class CreateProjectContractRequest extends BaseModel {

  private String projectId;

  private boolean isPrivate;

  private LinkedHashMap<String,Object> spec;

}
