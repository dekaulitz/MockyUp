package com.github.dekaulitz.mockyup.domain.mocks.vmodels;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDetailMocks {
    private String id;
    private String username;
    private String access;
    private Date updatedDate;
}
