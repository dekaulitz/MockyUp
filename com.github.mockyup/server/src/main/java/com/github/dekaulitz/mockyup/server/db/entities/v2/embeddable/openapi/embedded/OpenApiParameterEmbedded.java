package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiParameterPosition;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiParameterStyle;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
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
public class OpenApiParameterEmbedded implements Serializable {

  private String name;
  private OpenApiParameterPosition in;
  private String description;
  private Boolean required;
  private Boolean deprecated;
  private Boolean allowEmptyValue;
  private String $ref;
  private OpenApiParameterStyle style;
  private Boolean explode;
  private Boolean allowReserved;
  private Schema schema;
  private Map<String, OpenApiExampleEmbedded> examples = new HashMap<>();
  private Object example;
  private Content content;
  private Map<String, Object> extensions = new HashMap<>();

  public String get$ref() {
    return $ref;
  }

  public void set$ref(String $ref) {
    this.$ref = $ref;
  }


}
