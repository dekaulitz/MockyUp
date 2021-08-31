package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants;

public enum OpenApiEncodingType {
  FORM("form"),
  SPACE_DELIMITED("spaceDelimited"),
  PIPE_DELIMITED("pipeDelimited"),
  DEEP_OBJECT("deepObject");

  private String value;

  OpenApiEncodingType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
