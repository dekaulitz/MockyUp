package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded;

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
public class OpenApiHeaderEmbedded implements Serializable {

  private String description;
  private String $ref;
  private Boolean required;
  private Boolean deprecated;
  private OpenApiHeaderEmbedded.StyleEnum style;
  private Boolean explode;
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

  /**
   * Gets or Sets style
   */
  public enum StyleEnum {
    SIMPLE("simple");

    private final String value;

    StyleEnum(String value) {
      this.value = value;
    }
  }
}
