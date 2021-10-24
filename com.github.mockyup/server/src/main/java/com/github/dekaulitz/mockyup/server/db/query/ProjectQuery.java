package com.github.dekaulitz.mockyup.server.db.query;

import com.github.dekaulitz.mockyup.server.model.param.GetProjectParam;
import java.util.Arrays;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;

public class ProjectQuery extends BaseQuery<GetProjectParam> {

  @Override
  public void buildQuery(GetProjectParam getProjectParam) {
    this.setPageable(getProjectParam);
    this.id(getProjectParam.getId());
    this.idIn(getProjectParam.getIds());
    this.projectName(getProjectParam.getProjectName());
    this.tag(getProjectParam.getTag());

  }

  public ProjectQuery idIn(String[] idArray) {
    if (ArrayUtils.isNotEmpty(idArray)) {
      ObjectId[] ids = Arrays.stream(idArray).map(ObjectId::new)
          .toArray(ObjectId[]::new);
      criteriaSet.add(Criteria.where("_id").in(ids));
    }
    return this;
  }

  public ProjectQuery id(String id) {
    if (StringUtils.isNotBlank(id)) {
      this.criteriaSet.add(Criteria.where("_id").is(new ObjectId(id)));
    }
    return this;
  }

  public ProjectQuery projectName(String projectName) {
    if (StringUtils.isNotBlank(projectName)) {
      this.criteriaSet.add(Criteria.where("projectName").regex(".*" + projectName + ".*", "i"));
    }
    return this;
  }

  public ProjectQuery tag(String tag) {
    if (StringUtils.isNotBlank(tag)) {
      this.criteriaSet.add(Criteria.where("projectTags").is(tag));
    }
    return this;
  }
}
