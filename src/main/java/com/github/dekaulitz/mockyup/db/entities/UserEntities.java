package com.github.dekaulitz.mockyup.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
  private String username;
  private String password;
  private List<String> accessList;
  private Date updatedDate;

  @JsonIgnore
  public String getPassword() {
    return password;
  }
}
