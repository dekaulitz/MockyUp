package com.github.dekaulitz.mockyup.domain.mocks.vmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoMockLookupVmodel {
    private String id;
    private List<UserDetailMocks> users;


}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class UserDetailMocks {
    private String id;
    private String username;
    private String access;
    private Date updatedDate;
}
