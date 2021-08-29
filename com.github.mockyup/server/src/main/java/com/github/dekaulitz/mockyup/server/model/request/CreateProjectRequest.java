package com.github.dekaulitz.mockyup.server.model.request;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;
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
public class CreateProjectRequest implements Serializable {

  @NotBlank
  private String projectName;

  @NotBlank
  private String projectDescription;

  private Set<String> projectTags = new HashSet<>();

  private String gitUrl;
}
