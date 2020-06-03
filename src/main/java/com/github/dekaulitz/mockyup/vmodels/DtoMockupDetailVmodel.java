package com.github.dekaulitz.mockyup.vmodels;

import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoMockupDetailVmodel {
    private String id;
    private String title;
    private String description;
    private Object spec;
    private DtoMockupDetailUpdatedByVmodel updatedBy;
    private DtoMockupDetailCurrentAccessVmodel currentAccessUser;
    private Date dateUpdated;
}
