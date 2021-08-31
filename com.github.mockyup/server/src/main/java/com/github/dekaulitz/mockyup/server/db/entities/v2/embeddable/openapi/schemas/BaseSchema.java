package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.schemas;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiDiscriminatorEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiXmlEmbedded;
import io.swagger.v3.oas.models.ExternalDocumentation;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseSchema<T> implements Serializable {

  protected T _default;
  protected T example;
  protected List<T> _enum;
  private String name;
  private String title;
  private BigDecimal multipleOf;
  private BigDecimal maximum;
  private Boolean exclusiveMaximum;
  private BigDecimal minimum;
  private Boolean exclusiveMinimum;
  private Integer maxLength;
  private Integer minLength;
  private String pattern;
  private Integer maxItems;
  private Integer minItems;
  private Boolean uniqueItems;
  private Integer maxProperties;
  private Integer minProperties;
  private List<String> required;
  private String type;
  private BaseSchema not;
  private Map<String, BaseSchema> properties = new HashMap<>();
  private Object additionalProperties;
  private String description;
  private String format;
  private String $ref;
  private Boolean nullable;
  private Boolean readOnly;
  private Boolean writeOnly;
  private ExternalDocumentation externalDocs;
  private Boolean deprecated;
  private OpenApiXmlEmbedded xml;
  private Map<String, Object> extensions = new HashMap<>();
  private OpenApiDiscriminatorEmbedded discriminator;
  private BaseSchema reference$ref;

  public String get$ref() {
    return $ref;
  }

  public void set$ref(String $ref) {
    if ($ref != null && ($ref.indexOf(".") == -1 && $ref.indexOf("/") == -1)) {
      $ref = "#/components/schemas/" + $ref;
    }
    this.$ref = $ref;
  }
}
