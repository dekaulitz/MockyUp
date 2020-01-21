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
public class MockExample {
    private Map<String, Object> property;
    private MockResponse response;
}
