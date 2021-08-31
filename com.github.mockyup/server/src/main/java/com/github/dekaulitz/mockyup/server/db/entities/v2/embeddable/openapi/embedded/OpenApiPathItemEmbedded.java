package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded;

import io.swagger.v3.oas.models.parameters.Parameter;
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
public class OpenApiPathItemEmbedded implements Serializable {

  private String summary;
  private String description;
  private OpenApiPathOperationEmbedded get;
  private OpenApiPathOperationEmbedded put;
  private OpenApiPathOperationEmbedded post;
  private OpenApiPathOperationEmbedded delete;
  private OpenApiPathOperationEmbedded options;
  private OpenApiPathOperationEmbedded head;
  private OpenApiPathOperationEmbedded patch;
  private OpenApiPathOperationEmbedded trace;
  private List<Server> servers = new ArrayList<>();
  private List<Parameter> parameters = new ArrayList<>();
  private String $ref;
  private Map<String, Object> extensions = new HashMap<>();
}
