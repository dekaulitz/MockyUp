package com.github.dekaulitz.mockyup.infrastructure.db.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MockCreatorEntities {
    private String userId;
    private String username;
}
