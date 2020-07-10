package com.github.dekaulitz.mockyup.domain.auth.vmodels;

import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DtoAuthProfileVmodel {
    private String id;
    private String username;
    private String token;
    private List<String> accessMenus;
}
