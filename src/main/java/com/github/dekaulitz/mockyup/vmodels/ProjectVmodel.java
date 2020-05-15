package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectVmodel implements Serializable{

    private String id;

    private String name;

    private String description;
}
