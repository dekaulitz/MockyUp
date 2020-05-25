package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegistrationResponseVmodel {
    private String id;
    private String username;
    private List<String> accessList;
}
