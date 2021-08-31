package com.github.dekaulitz.mockyup.server.model.response;

import com.github.dekaulitz.mockyup.server.model.embeddable.TranslationsMessage;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

  private Integer httpCode;
  private Integer statusCode;
  private String description;
  private Set<TranslationsMessage> messages;
}
