package com.github.dekaulitz.mockyup.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MockResponseModel {
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
