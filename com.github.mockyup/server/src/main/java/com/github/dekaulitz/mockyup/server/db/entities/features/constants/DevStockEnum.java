package com.github.dekaulitz.mockyup.server.db.entities.v2.features.constants;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiContentType;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

public enum DevStockEnum {
  MOCKING_REQUEST("x-mock-request");
  private static final Map<String, DevStockEnum> lookup = new HashMap<>();

  static {
    for (DevStockEnum d : DevStockEnum.values()) {
      lookup.put(d.getAbbreviation(), d);
    }
  }

  @Getter
  private String value;

  DevStockEnum(String value) {
    this.value = value;
  }

  public static DevStockEnum get(String abbreviation) {
    return lookup.get(abbreviation);
  }

  public static boolean isValid(String abbreviation) {
    return lookup.containsKey(abbreviation);
  }

  public String getAbbreviation() {
    return value;
  }
}
