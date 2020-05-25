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
    private Long expiredTime;
    private String refreshToken;
    private List<String> accessMenus;
}
