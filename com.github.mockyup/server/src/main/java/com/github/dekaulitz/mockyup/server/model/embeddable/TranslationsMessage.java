package com.github.dekaulitz.mockyup.server.model.embeddable;

import java.io.Serializable;
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
public class TranslationsMessage implements Serializable {

  private String lang = "EN";
  private String message;
}
