package com.github.dekaulitz.mockyup.server.db.query;

import com.github.dekaulitz.mockyup.server.model.param.GetProjectContractParam;
import java.util.Arrays;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;

@NoArgsConstructor
public class ProjectContractQuery extends BaseQuery<GetProjectContractParam> {


  public void buildQuery(GetProjectContractParam getProjectContractParam) {
    this.idIn(getProjectContractParam.getIds());
    this.id(getProjectContractParam.getId());
    this.projectId(getProjectContractParam.getProjectId());
    this.setPageable(getProjectContractParam);
  }

  private ProjectContractQuery idIn(String[] idArray) {
    if (ArrayUtils.isNotEmpty(idArray)) {
      ObjectId[] ids = Arrays.stream(idArray).map(ObjectId::new)
          .toArray(ObjectId[]::new);
      criterias.add(Criteria.where("_id").in(ids));
    }
    return this;
  }

  public ProjectContractQuery id(String id) {
    if (StringUtils.isNotBlank(id)) {
      this.criterias.add(Criteria.where("id").is(new ObjectId(id)));
    }
    return this;
  }

  public ProjectContractQuery projectId(String projectId) {
    if (StringUtils.isNotBlank(projectId)) {
      this.criterias.add(Criteria.where("projectId").is(projectId));
    }
    return this;
  }

}
