package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiContactEmbedded;
import io.swagger.v3.oas.models.info.License;
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
public class OpenApiProjectInfoEmbedded implements Serializable {

  private String title;
  private String description;
  private String termsOfService;
  private OpenApiContactEmbedded contact;
  private License license;
  private String version;
  private Map<String, Object> extensions = new HashMap<>();

}
