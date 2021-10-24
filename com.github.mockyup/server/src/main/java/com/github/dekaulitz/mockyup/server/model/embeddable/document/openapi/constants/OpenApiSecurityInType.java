package com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.constants;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public enum OpenApiSecurityInType {
  COOKIE("cookie"),

  HEADER("header"),

  QUERY("query");

  private static final Map<String, OpenApiSecurityInType> lookup = new HashMap<>();

  static {
    for (OpenApiSecurityInType d : OpenApiSecurityInType.values()) {
      lookup.put(d.getAbbreviation(), d);
    }
  }

  @Getter
  private String value;

  OpenApiSecurityInType(String value) {
    this.value = value;
  }

  public static OpenApiSecurityInType get(String abbreviation) {
    return lookup.get(abbreviation);
  }

  public static boolean isValid(String abbreviation) {
    return lookup.containsKey(abbreviation);
  }

  public String getAbbreviation() {
    return value;
  }


}
