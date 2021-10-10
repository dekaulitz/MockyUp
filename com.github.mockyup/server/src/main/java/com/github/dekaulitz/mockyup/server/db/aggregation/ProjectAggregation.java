package com.github.dekaulitz.mockyup.server.db.aggregation;

import com.github.dekaulitz.mockyup.server.model.param.GetProjectTagsParam;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

public class ProjectAggregation {

  public static Aggregation getTaggingAggregation(
      GetProjectTagsParam getProjectTagsParam) {
    return Aggregation.newAggregation(
        Aggregation.project().and("projectTags").as("projectTags"),
        Aggregation.unwind("projectTags"),
        Aggregation.group("$projectTags").count().as("totalProjects"),
        Aggregation.project().andExclude("_id").and("$_id").as("tag").and("$totalProjects")
            .as("totalProjects"),
        Aggregation.match(
            Criteria.where("tag").regex(".*" + getProjectTagsParam.getTag() + ".*", "i"))
    );
  }
}
