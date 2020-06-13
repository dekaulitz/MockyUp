package com.github.dekaulitz.mockyup.vmodels;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.util.Json;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtoMockupHistoryVmodel {
    private String id;
    private String mockId;
    private String swagger;

    public Object getSwagger() throws JsonProcessingException {
        return Json.mapper().readTree(this.swagger);
    }


}
