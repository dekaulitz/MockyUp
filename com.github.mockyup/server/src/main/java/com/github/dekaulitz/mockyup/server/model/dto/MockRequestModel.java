package com.github.dekaulitz.mockyup.server.model.dto;

import com.github.dekaulitz.mockyup.server.model.common.BaseModel;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.mockup.MockingMatchingResponseEmbedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MockRequestModel extends BaseModel {

  private String path;
  private MockingMatchingResponseEmbedded response;
}
