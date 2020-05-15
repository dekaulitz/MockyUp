package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

import javax.validation.constraints.NotNull;

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
}
