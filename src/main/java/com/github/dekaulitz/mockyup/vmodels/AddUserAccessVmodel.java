package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddUserAccessVmodel {

    private String userId;
    private String access;
}
