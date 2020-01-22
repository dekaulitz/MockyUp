package com.github.dekaulitz.mockyup.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MockResponse {
    private int httpCode;
    private Object response;
}
