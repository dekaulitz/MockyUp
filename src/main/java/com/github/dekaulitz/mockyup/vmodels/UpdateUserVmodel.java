package com.github.dekaulitz.mockyup.vmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserVmodel {
    @NotNull
    private String username;
    @NotNull
    private String password;
    private List<String> accessList;
}
