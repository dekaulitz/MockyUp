package com.github.dekaulitz.mockyup.infrastructure.db.entities;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserMocksEntities implements Serializable {
    @Indexed
    private String userId;
    private String access;
}
