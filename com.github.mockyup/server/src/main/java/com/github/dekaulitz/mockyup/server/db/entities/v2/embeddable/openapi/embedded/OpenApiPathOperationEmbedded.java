package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiServerEmbedded;
import io.swagger.v3.oas.models.ExternalDocumentation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenApiPathOperationEmbedded implements Serializable {

  private List<String> tags;
  private String summary;
  private String description;
  private ExternalDocumentation externalDocs;
  private String operationId;
  private List<OpenApiParameterEmbedded> parameters = new ArrayList<>();
  private OpenApiRequestBodyEmbedded requestBody;
  private List<OpenApiPathResponseEmbedded> responses = new ArrayList<>();
  private Map<String, OpenApiCallbackEmbedded> callbacks = new HashMap<>();
  private Boolean deprecated;
  private List<OpenApiSecurityEmbedded> security = new ArrayList<>();
  private List<OpenApiServerEmbedded> servers = new ArrayList<>();
  private Map<String, Object> extensions = new HashMap<>();
}
