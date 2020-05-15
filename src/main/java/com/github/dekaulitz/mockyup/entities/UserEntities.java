package com.github.dekaulitz.mockyup.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

@Document
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntities implements Serializable {
    @Id
    private String id;
    private String fullName;
    private String email;
    private String password;
    private LinkedHashMap<String, List<String>> access;
}
