package com.github.dekaulitz.mockyup.server.domain.mocks.vmodels;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.PathItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DtoPathItemOpenApiVmodel {

  private PathItem pathItem;
  private String[] openAPIPaths;
  private String[] paths;
  private Components components;
}
