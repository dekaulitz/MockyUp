package com.github.dekaulitz.mockyup.server.model.dto;

import com.github.dekaulitz.mockyup.server.model.constants.Language;
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
public class Mandatory implements Serializable {

  private String requestId;
  private String agent;
  private String ip;
  private long requestTime;
  private Language language = Language.EN;
}
