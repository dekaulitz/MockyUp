package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiServerEmbedded;
import java.io.Serializable;
import java.util.HashMap;
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
public class OpenApiLinkEmbedded implements Serializable {

  private String operationRef;
  private String operationId;
  private Map<String, String> parameters = new HashMap<>();
  private Object requestBody;
  private Map<String, OpenApiHeaderEmbedded> headers = new HashMap<>();
  private String description;
  private String $ref;
  private Map<String, Object> extensions = new HashMap<>();
  private OpenApiServerEmbedded server;

  public String get$ref() {
    return $ref;
  }

  public void set$ref(String $ref) {
    this.$ref = $ref;
  }
}
