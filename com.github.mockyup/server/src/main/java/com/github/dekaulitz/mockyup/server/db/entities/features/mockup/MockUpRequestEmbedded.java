package com.github.dekaulitz.mockyup.server.db.entities.v2.features.mockup;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup.MockingMatchingRequestEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup.MockingMatchingResponseEmbedded;
import java.io.Serializable;
import java.util.LinkedList;
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

  private LinkedList<MockingMatchingRequestEmbedded> mockingRequestPaths;
  private LinkedList<MockingMatchingRequestEmbedded> mockingRequestHeaders;
  private LinkedList<MockingMatchingRequestEmbedded> mockingRequestQueries;
  private LinkedList<MockingMatchingRequestEmbedded> mockingRequestBodies;
  private MockingMatchingResponseEmbedded mockingDefaultResponse;

}
