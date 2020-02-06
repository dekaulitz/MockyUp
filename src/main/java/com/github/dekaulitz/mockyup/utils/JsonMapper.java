package com.github.dekaulitz.mockyup.utils;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.swagger.v3.parser.ObjectMapperFactory;

public class JsonMapper {
    private static com.fasterxml.jackson.databind.ObjectMapper mapper;
    private static com.fasterxml.jackson.databind.ObjectMapper pathMapper;
    private static com.fasterxml.jackson.databind.ObjectMapper responseMapper;

    public static com.fasterxml.jackson.databind.ObjectMapper mapper() {
        if (mapper == null) {
            mapper = ObjectMapperFactory.createJson();
        }
        return mapper;
    }

    public static ObjectWriter pretty() {
        return mapper().writer(new DefaultPrettyPrinter());
    }

    public static String pretty(Object o) {
        try {
            return pretty().writeValueAsString(o);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static void prettyPrint(Object o) {
        try {
            System.out.println(pretty().writeValueAsString(o).replace("\r", ""));
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }
}
