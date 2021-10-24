package com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.embedded;

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
public class OpenApiContactEmbedded implements Serializable {

  private String name;
  private String url;
  private String email;
  private Map<String, Object> extensions = new HashMap<>();
}
