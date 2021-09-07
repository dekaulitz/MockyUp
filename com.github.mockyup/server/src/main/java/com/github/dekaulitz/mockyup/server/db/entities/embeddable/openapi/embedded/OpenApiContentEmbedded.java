package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiContentType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.schemas.BaseSchema;
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
public class OpenApiContentEmbedded implements Serializable {

  private OpenApiContentType contentType;
  private BaseSchema schema;
  private Map<String, OpenApiExampleEmbedded> examples = new HashMap<>();
  private Object example;
  private Map<String, OpenApiEncodingEmbedded> encoding = new HashMap<>();
  private Map<String, Object> extensions = new HashMap<>();
}
