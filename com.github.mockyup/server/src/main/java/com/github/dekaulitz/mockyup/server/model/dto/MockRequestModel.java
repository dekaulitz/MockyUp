package com.github.dekaulitz.mockyup.server.model.dto;

import com.github.dekaulitz.mockyup.server.db.entities.embeddable.mockup.MockingMatchingResponseEmbedded;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MockRequestModel implements Serializable {

  private String path;
  private MockingMatchingResponseEmbedded response;
}
