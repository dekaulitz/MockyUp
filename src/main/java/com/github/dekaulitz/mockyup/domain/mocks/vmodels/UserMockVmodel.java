package com.github.dekaulitz.mockyup.domain.mocks.vmodels;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserMockVmodel {
    private String userId;
    private String access;
}
