package com.github.dekaulitz.mockyup.server.model.param;

import com.github.dekaulitz.mockyup.server.model.common.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString(callSuper = true)
public class GetProjectTagsParam extends BaseModel {

  private String tag;

}
