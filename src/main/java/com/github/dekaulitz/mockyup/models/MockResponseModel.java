package com.github.dekaulitz.mockyup.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MockResponseModel {
    private int httpCode;
    private Object response;
    private HashMap<String, Object> headers;
}
