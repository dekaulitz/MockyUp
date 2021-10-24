package com.github.dekaulitz.mockyup.server.configuration.mongodb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dekaulitz.mockyup.server.utils.JsonMapper;
import java.util.Map;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class ObjectNodeToMapConverter implements Converter<ObjectNode, Map<Object, Object>> {

  @Override
  public Map<Object, Object> convert(ObjectNode jsonNode) {
    return JsonMapper.mapper().convertValue(jsonNode, new TypeReference<Map<Object, Object>>() {
    });
  }
}
