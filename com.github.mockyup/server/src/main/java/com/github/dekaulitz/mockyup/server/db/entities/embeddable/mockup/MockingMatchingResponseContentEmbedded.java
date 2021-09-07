package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiContentType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MockingMatchingResponseContentEmbedded implements Serializable {

  private Object value;
  private String $ref;

  public String get$ref() {
    return $ref;
  }

  public void set$ref(String $ref) {
    this.$ref = $ref;
  }

}
