package com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.constants;

import java.util.HashMap;
import java.util.Map;

public enum OpenApiEncodingType {
  FORM("form"),
  SPACE_DELIMITED("spaceDelimited"),
  PIPE_DELIMITED("pipeDelimited"),
  DEEP_OBJECT("deepObject");

  private static final Map<String, OpenApiEncodingType> lookup = new HashMap<>();

  static {
    for (OpenApiEncodingType d : OpenApiEncodingType.values()) {
      lookup.put(d.getAbbreviation(), d);
    }
  }

  private String value;


  OpenApiEncodingType(String value) {
    this.value = value;
  }

  public static OpenApiEncodingType get(String abbreviation) {
    return lookup.get(abbreviation);
  }

  public static boolean isValid(String abbreviation) {
    return lookup.containsKey(abbreviation);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public String getAbbreviation() {
    return value;
  }
}
