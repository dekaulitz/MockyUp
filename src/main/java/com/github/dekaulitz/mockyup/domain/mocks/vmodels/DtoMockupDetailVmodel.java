package com.github.dekaulitz.mockyup.domain.mocks.vmodels;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.util.Json;
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
    private String spec;
    private DtoMockupDetailUpdatedByVmodel updatedBy;
    private DtoMockupDetailCurrentAccessVmodel currentAccessUser;
    private Date dateUpdated;


    public Object getSpec() throws JsonProcessingException {
        return Json.mapper().readTree(this.spec);
    }
}
