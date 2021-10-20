package com.github.dekaulitz.mockyup.server.model.embeddable;

import com.github.dekaulitz.mockyup.server.model.constants.Language;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

  @NotNull
  private Language lang;
  @NotBlank
  private String message;
}
