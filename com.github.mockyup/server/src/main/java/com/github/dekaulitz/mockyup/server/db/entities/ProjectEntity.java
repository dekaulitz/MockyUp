package com.github.dekaulitz.mockyup.server.db.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(callSuper = true)
public class ProjectEntity extends BaseMongo implements Serializable {

  private static final Long serialVersionUID = 1L;

  @NotBlank
  private String projectName;

  @NotBlank
  private String projectDescription;

  private Set<String> projectTags = new HashSet<>();

}
