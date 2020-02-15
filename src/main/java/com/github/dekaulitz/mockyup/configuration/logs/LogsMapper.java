package com.github.dekaulitz.mockyup.configuration.logs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.utils.JsonMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LogsMapper {

    public String logRequest(Map<String, Object> requestHeaders) {
        try {
            return JsonMapper.mapper().writeValueAsString(requestHeaders);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String logRequest(Object object) {
        try {
            return JsonMapper.mapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

    }
}
