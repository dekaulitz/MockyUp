package com.github.dekaulitz.mockyup.utils;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Json {
    private static ObjectMapper mapper;

   public static ObjectMapper mapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }
}
