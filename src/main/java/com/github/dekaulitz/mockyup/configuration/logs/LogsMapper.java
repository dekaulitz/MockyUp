package com.github.dekaulitz.mockyup.configuration.logs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.utils.JsonMapper;

import java.util.Map;

public class LogsMapper {

    public static String logRequest(Map<String, Object> requestHeaders) {
        try {
            return JsonMapper.mapper().writeValueAsString(requestHeaders);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }
}
