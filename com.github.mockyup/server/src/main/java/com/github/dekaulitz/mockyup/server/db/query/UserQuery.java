package com.github.dekaulitz.mockyup.server.db.query;

import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

@NoArgsConstructor
public class UserQuery extends BaseQuery<GetUserParam> {

  @Override
  public void buildQuery(GetUserParam getUserParam) {
    this.email(getUserParam.getEmail());
    this.username(getUserParam.getUsername());
    this.usernameOrEmail(getUserParam.getUserNameOrEmail());
    this.setPageable(getUserParam);
  }

  public void email(String email) {
    if (StringUtils.isNotBlank(email)) {
      this.criteriaSet.add(Criteria.where("email").is(email));
    }
  }

  public void username(String username) {
    if (StringUtils.isNotBlank(username)) {
      this.criteriaSet.add(Criteria.where("username").is(username));
    }
  }

  public void authUsernameOrEmail(String usernameOrEmail) {
    if (StringUtils.isNotBlank(usernameOrEmail)) {
      this.criteriaSet.add(new Criteria().orOperator(Criteria.where("username").is(usernameOrEmail),
          Criteria.where("email").is(usernameOrEmail)));
    }
  }

  public void usernameOrEmail(String username, String email) {
    if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(email)) {
      this.criteriaSet.add(new Criteria().orOperator(Criteria.where("username").is(username),
          Criteria.where("email").is(email)));
    }
  }

  public void usernameOrEmail(String usernameOrEmail) {
    if (StringUtils.isNotBlank(usernameOrEmail)) {
      this.criteriaSet.add(new Criteria().orOperator(Criteria.where("username").regex(".*" + usernameOrEmail + ".*", "i"),
          Criteria.where("email").regex(".*" + usernameOrEmail + ".*", "i")));
    }
  }
}
