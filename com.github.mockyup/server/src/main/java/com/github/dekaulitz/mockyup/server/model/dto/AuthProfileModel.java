package com.github.dekaulitz.mockyup.server.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.dekaulitz.mockyup.server.model.common.CustomAuthorityDeserializer;
import com.github.dekaulitz.mockyup.server.model.constants.Role;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AuthProfileModel implements UserDetails {

  private String username;
  // jwt token id
  private String jti;
  private boolean isAccountNonLocked;
  private boolean isEnabled;
  private String id;
  private String token;
  private List<GrantedAuthority> authorities = new ArrayList<>();
  private Set<Role> access = new HashSet<>();

  @JsonDeserialize(using = CustomAuthorityDeserializer.class)
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  public void setAuthorities(
      List<GrantedAuthority> authorities) {
    this.authorities = authorities;
  }

  @Override
  public String getPassword() {
    return null;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return this.isAccountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.isEnabled;
  }

  public void setAccess(Set<Role> access) {
    this.access = access;
    List<String> roles = access.stream().map(Enum::toString)
        .collect(Collectors.toList());
    this.setAuthorities(AuthorityUtils
        .commaSeparatedStringToAuthorityList(String.join(",", roles)));
  }
}
