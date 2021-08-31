package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
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

  private List<String> tags = new ArrayList<>();
  private String summary;
  private String description;
  private ExternalDocumentation externalDocs;
  private String operationId;
  private List<Parameter> parameters = new ArrayList<>();
  private RequestBody requestBody;
  private ApiResponses responses;
  private Map<String, Callback> callbacks = new HashMap<>();
  private Boolean deprecated;
  private List<SecurityRequirement> security = new ArrayList<>();
  private List<Server> servers = new ArrayList<>();
  private Map<String, Object> extensions = new HashMap<>();
}
