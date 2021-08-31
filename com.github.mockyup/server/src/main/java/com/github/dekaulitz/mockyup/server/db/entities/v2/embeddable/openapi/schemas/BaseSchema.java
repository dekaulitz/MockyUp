package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.schemas;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.XML;
import java.io.Serializable;
import java.math.BigDecimal;
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
  protected T example = null;
  protected List<T> _enum = null;
  private String name;
  private String title = null;
  private BigDecimal multipleOf = null;
  private BigDecimal maximum = null;
  private Boolean exclusiveMaximum = null;
  private BigDecimal minimum = null;
  private Boolean exclusiveMinimum = null;
  private Integer maxLength = null;
  private Integer minLength = null;
  private String pattern = null;
  private Integer maxItems = null;
  private Integer minItems = null;
  private Boolean uniqueItems = null;
  private Integer maxProperties = null;
  private Integer minProperties = null;
  private List<String> required = null;
  private String type = null;
  private Schema not = null;
  private Map<String, Schema> properties = null;
  private Object additionalProperties = null;
  private String description = null;
  private String format = null;
  private String $ref = null;
  private Boolean nullable = null;
  private Boolean readOnly = null;
  private Boolean writeOnly = null;
  private ExternalDocumentation externalDocs = null;
  private Boolean deprecated = null;
  private XML xml = null;
  private java.util.Map<String, Object> extensions = null;
  private Discriminator discriminator = null;
}
