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
public class OpenApiXmlEmbedded implements Serializable {

  private String name;
  private String namespace;
  private String prefix;
  private Boolean attribute;
  private Boolean wrapped;
  private Map<String, Object> extensions = new HashMap<>();
}
