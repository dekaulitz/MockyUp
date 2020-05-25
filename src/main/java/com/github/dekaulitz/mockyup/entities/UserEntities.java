package com.github.dekaulitz.mockyup.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
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
    private String username;
    private String password;
    private List<String> accessList;
}
