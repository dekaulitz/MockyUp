package com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded;

import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.constants.OpenApiSecurityInType;
import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.constants.OpenApiSecurityType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpenApiSecuritySchemaEmbedded implements Serializable {

  private OpenApiSecurityType type;
  private String description;
  private String name;
  private String $ref;
  private OpenApiSecurityInType in;
  private String scheme;
  private String bearerFormat;
  private OpenApiOauthFlowsEmbedded flows;
  private String openIdConnectUrl;
  private Map<String, Object> extensions = new HashMap<>();

  public String get$ref() {
    return $ref;
  }

  public void set$ref(String $ref) {
    this.$ref = $ref;
  }
}
