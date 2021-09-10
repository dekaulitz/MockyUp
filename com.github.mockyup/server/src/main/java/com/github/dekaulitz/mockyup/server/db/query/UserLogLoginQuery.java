package com.github.dekaulitz.mockyup.server.db.query;

import com.github.dekaulitz.mockyup.server.model.param.GetUserLogLoginParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

public class UserLogLoginQuery extends BaseQuery<GetUserLogLoginParam> {

  @Override
  public void buildQuery(GetUserLogLoginParam getUserLogLoginParam) {
    this.setPageable(getUserLogLoginParam);
    this.jti(getUserLogLoginParam.getJti());
    this.jti(getUserLogLoginParam.getJti());
  }

  public void jti(String jti) {
    if (StringUtils.isNotBlank(jti)) {
      this.criteriaSet.add(Criteria.where("jti").is(jti));
    }
  }

  public void userId(String userId) {
    if (StringUtils.isNotBlank(userId)) {
      this.criteriaSet.add(Criteria.where("userId").is(userId));
    }
  }
}
