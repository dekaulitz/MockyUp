package com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.models.ExternalDocumentation;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenApiTagEmbedded implements Serializable {

  private String name;
  private String description;
  private ExternalDocumentation externalDocs;
  private Map<String, Object> extensions = new HashMap<>();
}
