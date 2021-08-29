package com.github.dekaulitz.mockyup.server.model.response;

import com.github.dekaulitz.mockyup.server.db.entities.v1.MockCreatorEntities;
import com.github.dekaulitz.mockyup.server.db.entities.v1.UserMocksEntities;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class MockupResponse implements Serializable {

  private String id;

  private String title;

  private String description;

  private String version;

  private List<UserMocksEntities> users;

  private MockCreatorEntities updatedBy;

  private Date updatedDate;
}
