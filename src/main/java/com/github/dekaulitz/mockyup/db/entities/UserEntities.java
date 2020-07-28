package com.github.dekaulitz.mockyup.db.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
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
    private Date updatedDate;

    @JsonIgnore
    public String getPassword() {
        return password;
    }
}
