package com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.embedded;

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
public class OpenApiOauthFlowEmbedded implements Serializable {

  private String authorizationUrl;
  private String tokenUrl;
  private String refreshUrl;
  private Map<String, String> scopes = new HashMap<>();
  private Map<String, Object> extensions = new HashMap<>();

}
