package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
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
public class OpenApiPathResponseEmbedded implements Serializable {

  private Integer statusCode;
  private String name;
  private String description;
  @Valid
  private Map<String, OpenApiHeaderEmbedded> headers;
  @Valid
  private List<OpenApiContentEmbedded> content = new ArrayList<>();
  @Valid
  private Map<String, OpenApiLinkEmbedded> links = new HashMap<>();
  private Map<String, Object> extensions = new HashMap<>();
  private String $ref;

  public String get$ref() {
    return $ref;
  }

  public void set$ref(String $ref) {
    this.$ref = $ref;
  }
}
