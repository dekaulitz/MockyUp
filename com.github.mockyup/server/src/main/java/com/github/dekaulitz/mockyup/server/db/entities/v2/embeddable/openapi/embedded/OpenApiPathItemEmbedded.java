package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenApiPathItemEmbedded implements Serializable {

  private OpenApiPathOperationEmbedded get;
  private OpenApiPathOperationEmbedded put;
  private OpenApiPathOperationEmbedded post;
  private OpenApiPathOperationEmbedded delete;
  private OpenApiPathOperationEmbedded options;
  private OpenApiPathOperationEmbedded head;
  private OpenApiPathOperationEmbedded patch;
  private OpenApiPathOperationEmbedded trace;
}
