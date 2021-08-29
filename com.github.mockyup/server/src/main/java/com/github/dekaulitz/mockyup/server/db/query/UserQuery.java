package com.github.dekaulitz.mockyup.server.db.query;

import com.github.dekaulitz.mockyup.server.model.param.GetUserParam;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;

@NoArgsConstructor
public class UserQuery extends BaseQuery<GetUserParam> {


  public void buildQuery(GetUserParam getUserParam) {
    this.email(getUserParam.getEmail());
    this.setPageable(getUserParam);
  }

  private UserQuery email(String email) {
    if (StringUtils.isNotBlank(email)) {
      this.criterias.add(Criteria.where("email").is(new ObjectId(email)));
    }
    return this;
  }
}
