package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserLoginVmodel {
    private String username;
    private String password;
}
