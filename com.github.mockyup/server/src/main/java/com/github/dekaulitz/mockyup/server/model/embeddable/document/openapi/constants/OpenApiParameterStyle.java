package com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.constants;

import java.util.HashMap;
import java.util.Map;

public enum OpenApiParameterStyle {
  MATRIX("matrix"),
  LABEL("label"),
  FORM("form"),
  SIMPLE("simple"),
  SPACEDELIMITED("spaceDelimited"),
  PIPEDELIMITED("pipeDelimited"),
  DEEPOBJECT("deepObject");

  private static final Map<String, OpenApiParameterStyle> lookup = new HashMap<>();

  static {
    for (OpenApiParameterStyle d : OpenApiParameterStyle.values()) {
      lookup.put(d.getAbbreviation(), d);
    }
  }

  private String value;

  OpenApiParameterStyle(String value) {
    this.value = value;
  }

  public static OpenApiParameterStyle get(String abbreviation) {
    return lookup.get(abbreviation);
  }

  public static boolean isValid(String abbreviation) {
    return lookup.containsKey(abbreviation);
  }

  public String getAbbreviation() {
    return value;
  }


}
