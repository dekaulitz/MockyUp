package com.github.dekaulitz.mockyup.vmodels;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class AccessVmodel {
    private String username;
    private String token;
    private List<String> accessMenus;
}
