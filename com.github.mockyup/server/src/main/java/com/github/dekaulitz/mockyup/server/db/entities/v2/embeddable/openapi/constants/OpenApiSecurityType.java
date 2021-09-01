package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public enum OpenApiSecurityType {
  APIKEY("apiKey"),
  HTTP("http"),
  OAUTH2("oauth2"),
  OPENIDCONNECT("openIdConnect");

  private static final Map<String, OpenApiSecurityType> lookup = new HashMap<>();

  static {
    for (OpenApiSecurityType d : OpenApiSecurityType.values()) {
      lookup.put(d.getAbbreviation(), d);
    }
  }

  @Getter
  private String value;

  OpenApiSecurityType(String value) {
    this.value = value;
  }

  public static OpenApiSecurityType get(String abbreviation) {
    return lookup.get(abbreviation);
  }

  public static boolean isValid(String abbreviation) {
   return lookup.containsKey(abbreviation);
  }

  public String getAbbreviation() {
    return value;
  }


}
