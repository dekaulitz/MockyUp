package com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.embedded;


import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.constants.OpenApiContentType;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.constants.OpenApiEncodingType;
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
public class OpenApiEncodingEmbedded implements Serializable {

  private OpenApiContentType contentType;
  private Map<String, OpenApiHeaderEmbedded> headers = new HashMap<>();
  private OpenApiEncodingType style;
  private Boolean explode;
  private Boolean allowReserved;
  private Map<String, Object> extensions = new HashMap<>();

}
