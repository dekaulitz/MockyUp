package com.github.dekaulitz.mockyup.server.model.embeddable;

import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class ErrorResponseEmbedded implements Serializable {

  private String exception;
  private String description;
  private Object extraMessage;
  @NotNull
  @Valid
  private TranslationsMessage message;
}
