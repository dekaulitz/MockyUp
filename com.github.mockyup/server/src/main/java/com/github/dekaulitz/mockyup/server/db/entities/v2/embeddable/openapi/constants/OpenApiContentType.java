package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants;

import lombok.Getter;

public enum OpenApiContentType {
  APPLICATION_JSON("application/json"), APPLICATION_XML("application/xml");
  @Getter
  private String value;

  OpenApiContentType(String value) {
    this.value = value;
  }
}
