package com.github.dekaulitz.mockyup.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Log4j2
public class MockEntities implements Serializable {
    @Id
    private String id;
    @Field
    private String title;
    @Field
    private String description;
    @Field
    private String version;
    @Field
    private String spec;


}
