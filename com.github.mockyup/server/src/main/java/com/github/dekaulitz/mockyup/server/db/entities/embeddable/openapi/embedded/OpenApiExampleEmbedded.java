package com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded;

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
public class OpenApiExampleEmbedded implements Serializable {

  private String summary;
  private String description;
  private Object value;
  private String externalValue;
  private String $ref;
  private Map<String, Object> extensions = new HashMap<>();

  public String get$ref() {
    return $ref;
  }

  public void set$ref(String $ref) {
    this.$ref = $ref;
  }
}
