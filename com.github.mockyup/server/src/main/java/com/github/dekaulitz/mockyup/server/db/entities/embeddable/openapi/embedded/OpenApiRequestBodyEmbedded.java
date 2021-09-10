package com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpenApiRequestBodyEmbedded implements Serializable {

  private String description;
  @Valid
  private List<OpenApiContentEmbedded> content = new ArrayList<>();
  private Boolean required;
  private Map<String, Object> extensions = new HashMap<>();
  private String $ref;


  public String get$ref() {
    return $ref;
  }

  public void set$ref(String $ref) {
    this.$ref = $ref;
  }
}
