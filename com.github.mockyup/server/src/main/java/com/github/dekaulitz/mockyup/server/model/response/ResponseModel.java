package com.github.dekaulitz.mockyup.server.model.response;

import com.github.dekaulitz.mockyup.server.model.dto.ErrorMessageModel;
import com.github.dekaulitz.mockyup.server.model.dto.MandatoryModel;
import com.github.dekaulitz.mockyup.server.model.embeddable.ErrorResponseEmbedded;
import com.github.dekaulitz.mockyup.server.model.embeddable.TranslationsMessage;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
public class ResponseModel implements Serializable {

  @NotNull
  private int statusCode;
  private String message;
  private Object data;
  @Valid
  private ErrorResponseEmbedded error;
  @NotBlank
  private String requestId;
  @NotNull
  private Long requestTime;

  public static ResponseModel initErrorResponse(ErrorMessageModel errorMessageModel,
      MandatoryModel mandatoryModel, Exception exception) {
    errorMessageModel = errorMessageModel.filterTranslation(mandatoryModel.getLanguage());
    TranslationsMessage message = errorMessageModel.getMessages().stream().findFirst().orElse(null);

    ErrorResponseEmbedded error = ErrorResponseEmbedded.builder()
        .exception(exception.getClass().getSimpleName())
        .description(errorMessageModel.getDescription())
        // it can be replaced with exception trace message or message
        .extraMessage(errorMessageModel.getDetailMessage())
        .message(message)
        .build();
    return ResponseModel.builder()
        .statusCode(errorMessageModel.getStatusCode())
        .message(errorMessageModel.getDescription())
        .requestId(mandatoryModel.getRequestId())
        .error(error)
        .requestTime(mandatoryModel.getRequestTime())
        .build();
  }

  public static ResponseModel initSuccessResponse(Object data, MandatoryModel mandatoryModel) {
    return ResponseModel.builder()
        .statusCode(2000)
        .message("success")
        .requestId(mandatoryModel.getRequestId())
        .requestTime(mandatoryModel.getRequestTime())
        .data(data)
        .build();
  }
}
