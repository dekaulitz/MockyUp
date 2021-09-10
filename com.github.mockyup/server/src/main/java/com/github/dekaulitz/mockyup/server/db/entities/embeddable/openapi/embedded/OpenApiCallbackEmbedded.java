package com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded;

import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.OpenApiPathEmbedded;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpenApiCallbackEmbedded implements Serializable {

  private List<OpenApiPathEmbedded> paths = new ArrayList<>();
  private Map<String, Object> extensions = new HashMap<>();
  private String $ref = null;

  /**
   * @since 2.0.3
   */
  public String get$ref() {
    return $ref;
  }

  /**
   * @since 2.0.3
   */
  public void set$ref(String $ref) {
    if ($ref != null && ($ref.indexOf(".") == -1 && $ref.indexOf("/") == -1)) {
      $ref = "#/components/callbacks/" + $ref;
    }
    this.$ref = $ref;
  }
}
