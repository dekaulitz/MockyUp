package com.github.dekaulitz.mockyup.server.model.response;

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

public class ResponseModel implements Serializable {

  private String status;
  private String statusCode;
  private Object data;
  private Integer requestTime;
}
