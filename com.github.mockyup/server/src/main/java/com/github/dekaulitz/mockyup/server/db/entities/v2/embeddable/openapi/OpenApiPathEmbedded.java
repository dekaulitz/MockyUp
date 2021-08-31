package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiPathHttpMethod;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiPathResponse;
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
public class OpenApiPathEmbedded implements Serializable {

  private String path;
  private OpenApiPathHttpMethod httpMethod;
  private List<OpenApiPathResponse> responses = new ArrayList<>();
  private List<String> tags = new ArrayList<>();
  private String summary;
  private String description;
  private ExternalDocumentation externalDocs;
  private Map<String, Object> extensions = new HashMap<>();
  private String operationId;

//  private List<Parameter> parameters ;
//  private RequestBody requestBody ;
//  private ApiResponses responses ;
//  private Map<String, Callback> callbacks ;
//  private Boolean deprecated ;
//  private List<SecurityRequirement> security ;
//  private List<Server> servers ;

//  private OpenApiPathOperationEmbedded operation;
}
