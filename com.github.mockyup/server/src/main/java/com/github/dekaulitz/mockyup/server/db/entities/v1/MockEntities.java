package com.github.dekaulitz.mockyup.server.db.entities.v1;

import com.github.dekaulitz.mockyup.server.db.entities.v2.BaseMongo;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "mockup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockEntities extends BaseMongo implements Serializable {

  @Field
  private String title;
  @Field
  private String description;
  @Field
  private String spec;
  @Field
  private String swagger;
  @Field
  @Indexed
  private List<UserMocksEntities> users;
  private MockCreatorEntities updatedBy;
}
