package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RegistrationVmodel {
    @NotEmpty(message = "Please provide a username")
    private String username;
    @NotEmpty(message = "Please provide a password")
    private String password;
    private List<String> accessList;
}
