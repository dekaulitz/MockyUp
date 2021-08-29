package com.github.dekaulitz.mockyup.server.db.entities.v2;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectEntities extends BaseMongo implements Serializable {

  private static final Long serialVersionUID = 1L;

  @NotBlank
  private String projectName;

  @NotBlank
  private String projectDescription;

  private Set<String> projectTags = new HashSet<>();

  private String gitUrl;

}
