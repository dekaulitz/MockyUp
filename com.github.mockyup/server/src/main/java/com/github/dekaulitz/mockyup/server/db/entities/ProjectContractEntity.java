package com.github.dekaulitz.mockyup.server.db.entities;

import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.OpenApiComponents;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.OpenApiPathEmbedded;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.OpenApiProjectInfoEmbedded;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.OpenApiServerEmbedded;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.OpenApiTagEmbedded;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.embedded.OpenApiSecurityEmbedded;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "projectContracts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(callSuper = true)
public class ProjectContractEntity extends BaseMongo implements Serializable {

  public static final String COLLECTION_NAME = "projectContracts";
  private static final Long serialVersionUID = 1L;
  @NotNull
  private String projectId;
  private boolean isPrivate;

  private String openApiVersion;
  @Valid
  private OpenApiProjectInfoEmbedded info;
  @Valid
  private List<OpenApiServerEmbedded> servers = new ArrayList<>();
  @Valid
  private List<OpenApiSecurityEmbedded> security = new ArrayList<>();
  @Valid
  private Set<OpenApiTagEmbedded> tags = new HashSet<>();
  @Valid
  private List<OpenApiPathEmbedded> paths = new ArrayList<>();
  @Valid
  private OpenApiComponents components;

  private String rawSpecs;
}
