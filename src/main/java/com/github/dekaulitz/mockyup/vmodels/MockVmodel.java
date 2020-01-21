package com.github.dekaulitz.mockyup.vmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MockVmodel {
    private String id;
    private String title;
    private String description;
    private Object spec;
}
