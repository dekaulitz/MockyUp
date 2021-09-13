package com.github.dekaulitz.mockyup.server.model.request;

import com.github.dekaulitz.mockyup.server.model.common.BaseModel;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class CreateProjectRequest extends BaseModel {

  @NotBlank
  private String projectName;

  @NotBlank
  private String projectDescription;

  @NotNull
  private Set<String> projectTags;
}
