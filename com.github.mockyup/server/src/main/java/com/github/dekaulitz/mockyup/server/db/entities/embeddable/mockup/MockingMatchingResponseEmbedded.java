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
@Setter
@Getter
public class MockingMatchingResponseEmbedded implements Serializable {

  private Integer statusCode;
  private Map<String, Object> headers = new HashMap<>();
  private Map<OpenApiContentType, MockingMatchingResponseContentEmbedded> content;
}
