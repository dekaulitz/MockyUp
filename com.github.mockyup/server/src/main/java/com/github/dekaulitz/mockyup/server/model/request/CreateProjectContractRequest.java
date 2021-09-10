package com.github.dekaulitz.mockyup.server.model.request;

import java.io.Serializable;
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
public class CreateProjectContractRequest implements Serializable {

  private String projectId;

  private boolean isPrivate;

  private Object spec;

}
