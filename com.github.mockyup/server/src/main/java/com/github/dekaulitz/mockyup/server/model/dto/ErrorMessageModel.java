package com.github.dekaulitz.mockyup.server.model.dto;

import com.github.dekaulitz.mockyup.server.model.constants.Language;
import com.github.dekaulitz.mockyup.server.model.common.MultiTranslations;
import com.github.dekaulitz.mockyup.server.model.embeddable.TranslationsMessage;
import java.io.Serializable;
import java.util.Set;
import javax.validation.Valid;
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
public class ErrorMessageModel extends
    MultiTranslations<ErrorMessageModel> implements Serializable {

  @NotNull
  private Integer httpCode;
  @NotNull
  private Integer statusCode;
  private String description;
  // it can be replaced by error message from exception.message
  private String detailMessage;
  @NotNull
  @Valid
  private Set<TranslationsMessage> messages;

  @Override
  public ErrorMessageModel filterTranslation(Language language) {
    this.messages.removeIf(translationsMessage -> translationsMessage.getLang() != language);
    return this;
  }
}
