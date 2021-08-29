package com.github.dekaulitz.mockyup.server.domain.mocks.vmodels;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.util.Json;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoMockupHistoryVmodel {

  private String id;
  private String mockId;
  private Object swagger;


  public Object getSwagger() throws JsonProcessingException {
    return Json.mapper().readTree((String) this.swagger);
  }


}
