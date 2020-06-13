package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddUserAccessVmodel {
    @NotEmpty(message = "Please provide a userId")
    private String userId;
    @NotEmpty(message = "Please provide a access")
    private String access;
}
