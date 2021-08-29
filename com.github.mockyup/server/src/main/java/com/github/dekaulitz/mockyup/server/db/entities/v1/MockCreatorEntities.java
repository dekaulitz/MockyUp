package com.github.dekaulitz.mockyup.server.db.entities.v1;

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
public class MockCreatorEntities {

  private String userId;
  private String username;
}
