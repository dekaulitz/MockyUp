package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

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
    private String title;
    private String description;
    @NotNull
    private String projectId;
    private Object spec;

    private List<UserMocks> users;
    private Creator updatedBy;
    private Date dateUpdated;
}

