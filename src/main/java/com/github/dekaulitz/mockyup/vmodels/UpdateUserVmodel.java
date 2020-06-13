package com.github.dekaulitz.mockyup.vmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserVmodel {
    @NotEmpty(message = "Please provide a username")
    private String username;
    private String password;
    private List<String> accessList;
}
