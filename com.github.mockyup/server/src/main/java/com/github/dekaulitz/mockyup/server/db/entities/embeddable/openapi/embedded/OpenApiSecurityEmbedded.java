package com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenApiSecurityEmbedded extends LinkedHashMap<String, List<String>> {


  public OpenApiSecurityEmbedded addList(String name, String item) {
    this.put(name, Arrays.asList(item));
    return this;
  }

  public OpenApiSecurityEmbedded addList(String name, List<String> item) {
    this.put(name, item);
    return this;
  }

  public OpenApiSecurityEmbedded addList(String name) {
    this.put(name, new ArrayList<>());
    return this;
  }
}
