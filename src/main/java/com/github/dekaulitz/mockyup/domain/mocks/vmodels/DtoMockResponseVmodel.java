package com.github.dekaulitz.mockyup.domain.mocks.vmodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DtoMockResponseVmodel {
    private int httpCode;
    private Object response;
    private Map<String, Object> headers;

    public Object get$ref() {
        return $ref;
    }

    public void set$ref(Object $ref) {
        this.$ref = $ref;
    }

    private Object $ref;

}
