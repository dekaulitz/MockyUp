package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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

    private List<UserMocks> users;
    private CreatorVmodel updatedBy;
    private Date dateUpdated;
}

