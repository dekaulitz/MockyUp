package com.github.dekaulitz.mockyup.server.db.query;

import com.github.dekaulitz.mockyup.server.model.param.GetMockUpParam;
import java.util.Arrays;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;

@NoArgsConstructor
public class MockUpQuery extends BaseQuery<GetMockUpParam> {


  public void buildQuery(GetMockUpParam getMockUpParam) {
    this.idIn(getMockUpParam.getIds());
    this.id(getMockUpParam.getId());
    this.title(getMockUpParam.getTittle());
    this.setPageable(getMockUpParam);
  }

  private MockUpQuery idIn(String[] idArray) {
    if (ArrayUtils.isNotEmpty(idArray)) {
      ObjectId[] ids = Arrays.stream(idArray).map(ObjectId::new)
          .toArray(ObjectId[]::new);
      criterias.add(Criteria.where("_id").in(ids));
    }
    return this;
  }

  public MockUpQuery id(String id) {
    if (StringUtils.isNotBlank(id)) {
      this.criterias.add(Criteria.where("id").is(new ObjectId(id)));
    }
    return this;
  }

  public MockUpQuery title(String title) {
    if (StringUtils.isNotBlank(title)) {
      this.criterias.add(Criteria.where("title").regex(".*" + title + ".*", "i"));
    }
    return this;
  }

}
