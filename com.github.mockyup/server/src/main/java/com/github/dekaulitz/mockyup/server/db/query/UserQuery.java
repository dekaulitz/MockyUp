package com.github.dekaulitz.mockyup.server.db.query;

import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;

@NoArgsConstructor
public class UserQuery extends BaseQuery<GetUserParam> {

  @Override
  public void buildQuery(GetUserParam getUserParam) {
    this.email(getUserParam.getEmail());
    this.username(getUserParam.getUsername());
    this.setPageable(getUserParam);
  }

  private UserQuery email(String email) {
    if (StringUtils.isNotBlank(email)) {
      this.criteriaSet.add(Criteria.where("email").is(new ObjectId(email)));
    }
    return this;
  }

  private UserQuery username(String username) {
    if (StringUtils.isNotBlank(username)) {
      this.criteriaSet.add(Criteria.where("username").is(new ObjectId(username)));
    }
    return this;
  }
}
