package com.github.dekaulitz.mockyup.server.db.entities.v2.features.mockup;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup.MockingMatchingAttributeEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup.MockingMatchingRequestEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup.MockingMatchingResponseEmbedded;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MockUpRequestEmbedded implements Serializable {

  private List<MockingMatchingRequestEmbedded> mockingRequestHeaders;
  private List<MockingMatchingRequestEmbedded> mockingRequestPaths;
  private List<MockingMatchingRequestEmbedded> mockingRequestQueries;
  private List<MockingMatchingRequestEmbedded> mockingRequestBodies;
  private MockingMatchingResponseEmbedded responseDefault;

}
