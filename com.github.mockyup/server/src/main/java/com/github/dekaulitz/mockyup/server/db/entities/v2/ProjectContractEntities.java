package com.github.dekaulitz.mockyup.server.db.entities.v2;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiProjectInfoEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiServerEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiTagEmbedded;
import java.io.Serializable;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "projectContracts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectContractEntities extends BaseMongo implements Serializable {

  private static final Long serialVersionUID = 1L;

  @NotNull
  private String projectId;

  private String openApiVersion;
  @Valid
  private OpenApiProjectInfoEmbedded info;
  @Valid
  private Set<OpenApiServerEmbedded> servers;
  @Valid
  private Set<OpenApiTagEmbedded> tags;


  private String rawSpecs;
}
