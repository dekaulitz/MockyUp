package com.github.dekaulitz.mockyup.server.model.embeddable.document.mockup;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MockingMatchingResponseContentEmbedded implements Serializable {

  private Object value;
  private String $ref;

  public String get$ref() {
    return $ref;
  }

  public void set$ref(String $ref) {
    this.$ref = $ref;
  }

}
