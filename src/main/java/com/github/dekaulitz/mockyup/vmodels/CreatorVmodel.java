package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreatorVmodel {
    private String userId;
    private String username;
}
