package com.github.dekaulitz.mockyup.server.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.server.utils.JsonMapper;
import java.util.Map;
import org.springframework.stereotype.Component;

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
