package com.github.dekaulitz.mockyup.domain.auth.vmodels;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter

public class DtoAuthProfileVmodel {
    private String id;
    private String username;
    private String token;
    private List<String> accessMenus;
}
