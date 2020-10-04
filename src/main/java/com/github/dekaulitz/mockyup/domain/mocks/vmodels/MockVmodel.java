package com.github.dekaulitz.mockyup.domain.mocks.vmodels;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MockVmodel {

  private String id;

  @NotEmpty(message = "Please provide a title")
  private String title;
  @NotEmpty(message = "Please provide a description")
  private String description;
  @NotNull(message = "Please provide a swagger spec")
  private Object spec;

  private List<UserMockVmodel> users;
  private CreatorMockVmodel updatedBy;
  private Date dateUpdated;
}

