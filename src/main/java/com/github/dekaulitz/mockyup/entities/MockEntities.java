package com.github.dekaulitz.mockyup.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;

@Document(collection = "mockup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockEntities implements Serializable {
    @Id
    private String id;
    @Field
    private String title;
    @Field
    private String description;
    @Field
    private String version;

    private String projectId;
    @Field
    private String spec;
    @Field
    private String swagger;

    private Date createdDate;

    private Date updatedDate;
}
