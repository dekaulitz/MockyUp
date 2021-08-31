package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants;

public enum OpenApiParameterStyle {
  MATRIX("matrix"),
  LABEL("label"),
  FORM("form"),
  SIMPLE("simple"),
  SPACEDELIMITED("spaceDelimited"),
  PIPEDELIMITED("pipeDelimited"),
  DEEPOBJECT("deepObject");

  private String value;

  OpenApiParameterStyle(String value) {
    this.value = value;
  }
}
