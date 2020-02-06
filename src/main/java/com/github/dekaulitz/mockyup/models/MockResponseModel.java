package com.github.dekaulitz.mockyup.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MockResponseModel {
    private int httpCode;
    private Object response;
}
