package com.github.dekaulitz.mockyup.vmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

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
