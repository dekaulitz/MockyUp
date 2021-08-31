package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants;

import lombok.Getter;

public enum OpenApiSecurityType {
  APIKEY("apiKey"),
  HTTP("http"),
  OAUTH2("oauth2"),
  OPENIDCONNECT("openIdConnect");

  @Getter
  private String value;

  OpenApiSecurityType(String value) {
    this.value = value;
  }

}
