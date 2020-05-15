package com.github.dekaulitz.mockyup.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectEntities implements Serializable {

    @Id
    private String id;
    @NotNull
    private String name;
    @NotNull
    private String description;
}
