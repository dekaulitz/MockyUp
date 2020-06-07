package com.github.dekaulitz.mockyup.db.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Document(collection = "mockup_histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockHistoryEntities implements Serializable {
    @Id
    private String id;

    private String mockId;
    @Field
    private String title;
    @Field
    private String description;
    @Field
    private String version;
    @Field
    private String spec;
    @Field
    private String swagger;
    @Field
    @Indexed
    private List<UserMocksEntities> users;
    private MockCreatorEntities updatedBy;
    private Date updatedDate;
}
