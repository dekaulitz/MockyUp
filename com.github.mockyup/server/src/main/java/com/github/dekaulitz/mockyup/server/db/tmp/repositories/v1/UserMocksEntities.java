package com.github.dekaulitz.mockyup.server.db.tmp.repositories.v1;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserMocksEntities implements Serializable {

  @Indexed
  private String userId;
  private String access;
}
