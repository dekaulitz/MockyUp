package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RegistrationVmodel {
    private String username;
    private String password;
    private List<String> accessList;
}
