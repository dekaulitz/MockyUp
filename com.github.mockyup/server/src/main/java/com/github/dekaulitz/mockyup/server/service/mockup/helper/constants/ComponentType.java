package com.github.dekaulitz.mockyup.server.service.mockup.helper.constants;

import lombok.Getter;

/**
 * @see {@link io.swagger.v3.oas.models.Components}
 *
 *
 * only supported defined component
 */
public enum ComponentType {
  SCHEMAS("schemas"), PARAMETERS("parameters"), EXAMPLES("examples"), HEADERS("headers"), RESPONSES(
      "responses");
  @Getter
  private String type;

  ComponentType(String type) {
    this.type = type;
  }
}
