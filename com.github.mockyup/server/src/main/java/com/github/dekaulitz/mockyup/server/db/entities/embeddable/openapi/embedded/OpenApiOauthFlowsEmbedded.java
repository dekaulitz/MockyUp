package com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded;

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
public class OpenApiOauthFlowsEmbedded implements Serializable {

  private OpenApiOauthFlowEmbedded implicit;
  private OpenApiOauthFlowEmbedded password;
  private OpenApiOauthFlowEmbedded clientCredentials;
  private OpenApiOauthFlowEmbedded authorizationCode;
  private Map<String, Object> extensions = new HashMap<>();

}
