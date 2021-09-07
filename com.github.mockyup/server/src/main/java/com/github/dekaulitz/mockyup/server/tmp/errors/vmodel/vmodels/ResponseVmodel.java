package com.github.dekaulitz.mockyup.server.tmp.errors.vmodel.vmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
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
public class ResponseVmodel {

  @JsonProperty("response_message")
  private String responseMessage;
  @JsonProperty("response_code")
  private String responseCode;
  @JsonProperty("requestId")
  private String requestId;
  private Object data;
  private List<String> extraMessages;
}
