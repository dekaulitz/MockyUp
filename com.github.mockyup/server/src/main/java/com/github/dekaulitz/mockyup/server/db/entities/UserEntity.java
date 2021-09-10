package com.github.dekaulitz.mockyup.server.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.dekaulitz.mockyup.server.db.entities.embeddable.UserAccessProjectEmbedded;
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
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString(callSuper = true)
public class UserEntity extends BaseMongo implements Serializable {

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

  private boolean isAccountNonLocked;

  private boolean isEnabled;

  @JsonIgnore
  public String getPassword() {
    return password;
  }
}
