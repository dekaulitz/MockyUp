package com.github.dekaulitz.mockyup.server.model.dto;

import com.github.dekaulitz.mockyup.server.model.common.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ProjectTagsModel extends BaseModel {

  private String tag;
  private int totalProjects;
}
