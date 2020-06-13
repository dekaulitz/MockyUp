package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserLoginVmodel {
    @NotEmpty(message = "Please provide a username")
    private String username;
    @NotEmpty(message = "Please provide a password")
    private String password;
}
