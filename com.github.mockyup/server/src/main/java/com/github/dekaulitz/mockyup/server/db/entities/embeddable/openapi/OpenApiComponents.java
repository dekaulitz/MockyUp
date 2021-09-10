package com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded.OpenApiCallbackEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded.OpenApiExampleEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded.OpenApiHeaderEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded.OpenApiLinkEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded.OpenApiParameterEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded.OpenApiPathResponseEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded.OpenApiRequestBodyEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded.OpenApiSecuritySchemaEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.schemas.BaseSchema;
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
public class OpenApiComponents implements Serializable {

  private Map<String, BaseSchema> schemas = new HashMap<>();
  private List<OpenApiPathResponseEmbedded> responses = new ArrayList<>();
  private Map<String, OpenApiParameterEmbedded> parameters = new HashMap<>();
  private Map<String, OpenApiExampleEmbedded> examples = new HashMap<>();
  private Map<String, OpenApiRequestBodyEmbedded> requestBodies = new HashMap<>();
  private Map<String, OpenApiHeaderEmbedded> headers = new HashMap<>();
  private Map<String, OpenApiSecuritySchemaEmbedded> securitySchemes = new HashMap<>();
  private Map<String, OpenApiLinkEmbedded> links = new HashMap<>();
  private Map<String, OpenApiCallbackEmbedded> callbacks = new HashMap<>();
  private Map<String, Object> extensions = new HashMap<>();
}

