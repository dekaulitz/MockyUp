package com.github.dekaulitz.mockyup.server.db.entities.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntities implements Serializable {

  @Id
  private String id;
  @NotNull
  private String username;
  @NotNull
  private String email;
  @NotNull
  private String password;
  @NotNull
  private List<String> accessList;
  private Date updatedDate;

  @JsonIgnore
  public String getPassword() {
    return password;
  }
}
