package com.github.dekaulitz.mockyup.server.model.embeddable;

import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Message implements Serializable {

  private Integer httpCode;
  private Integer statusCode;
  private String description;
  private Set<TranslationsMessage> messages;

}
