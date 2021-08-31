package com.github.dekaulitz.mockyup.server.model.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class CreateProjectContractRequest implements Serializable {

  private String projectId;

  private boolean isPrivate;

  private Object spec;

}
