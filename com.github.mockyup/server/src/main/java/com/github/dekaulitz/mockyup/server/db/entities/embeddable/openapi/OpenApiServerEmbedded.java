package com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.models.servers.ServerVariables;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenApiServerEmbedded {

  /**
   * example  http://{host}/mocks/mocking/{testing_path}
   */
  private String url;
  private String description;
  /**
   * using default Class from open api \n variable will injecting into string base on variable that
   * defined on url example : host with value localhost:3030 it will replacing url to
   * http://localhost:3030/mocks/mocking
   */
  private ServerVariables variables;
  private Map<String, Object> extensions = new HashMap<>();
}
