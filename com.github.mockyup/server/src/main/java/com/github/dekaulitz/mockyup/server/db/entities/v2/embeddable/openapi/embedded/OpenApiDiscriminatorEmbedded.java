package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded;

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
public class OpenApiDiscriminatorEmbedded implements Serializable {

  private String propertyName;
  private Map<String, String> mapping = new HashMap<>();
}
