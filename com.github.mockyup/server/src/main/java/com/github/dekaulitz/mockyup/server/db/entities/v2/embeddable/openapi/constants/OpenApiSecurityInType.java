package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants;

import lombok.Getter;

public enum OpenApiSecurityInType {
  COOKIE("cookie"),

  HEADER("header"),

  QUERY("query");

  @Getter
  private String value;

  OpenApiSecurityInType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
