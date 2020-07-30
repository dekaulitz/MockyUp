package com.github.dekaulitz.mockyup.domain.mocks.vmodels;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DtoMockUserLookupVmodel {
    private String id;
    private List<UserDetailMocks> users;
}

