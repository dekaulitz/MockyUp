package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public enum OpenApiContentType {
  APPLICATION_JSON("application/json"), APPLICATION_XML("application/xml");
  private static final Map<String, OpenApiContentType> lookup = new HashMap<>();

  static {
    for (OpenApiContentType d : OpenApiContentType.values()) {
      lookup.put(d.getAbbreviation(), d);
    }
  }

  @Getter
  private String value;

  OpenApiContentType(String value) {
    this.value = value;
  }

  public static OpenApiContentType get(String abbreviation) {
    return lookup.get(abbreviation);
  }

  public static boolean isValid(String abbreviation) {
    return lookup.containsKey(abbreviation);
  }

  public String getAbbreviation() {
    return value;
  }
}
