package com.github.dekaulitz.mockyup.server.model.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class IssuerRequestModel implements Serializable {

  private String requestId;
  private String agent;
  private String ip;
}
