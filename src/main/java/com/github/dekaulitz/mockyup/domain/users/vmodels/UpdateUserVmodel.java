package com.github.dekaulitz.mockyup.domain.users.vmodels;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserVmodel {
    @NotEmpty(message = "Please provide a username")
    private String username;
    private String password;
    private List<String> accessList;
}
