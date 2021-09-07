package com.github.dekaulitz.mockyup.server.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.UserAccessProjectEmbedded;
import com.github.dekaulitz.mockyup.server.model.constants.Role;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntities extends BaseMongo implements Serializable {

  @NotBlank
  private String username;
  @NotBlank
  private String email;
  @NotBlank
  private String password;

  @Valid
  private Set<UserAccessProjectEmbedded> accessProjects = new HashSet<>();

  @NotNull
  private Set<Role> access;

  @JsonIgnore
  public String getPassword() {
    return password;
  }
}
