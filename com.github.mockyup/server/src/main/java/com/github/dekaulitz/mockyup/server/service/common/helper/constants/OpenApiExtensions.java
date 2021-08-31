package com.github.dekaulitz.mockyup.server.service.common.helper.constants;

import lombok.Getter;

public enum OpenApiExtensions {
  EX_MOCKING_RESPONSE_PROPERTIES("x-examples"), EX_CODEGEN_PROPERTIES("x-codegen-properties");
  @Getter
  private final String definitions;

  OpenApiExtensions(String definitions) {
    this.definitions = definitions;
  }
}
