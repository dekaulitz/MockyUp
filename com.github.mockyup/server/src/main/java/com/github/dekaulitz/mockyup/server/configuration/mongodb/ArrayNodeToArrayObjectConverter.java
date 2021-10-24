package com.github.dekaulitz.mockyup.server.configuration.mongodb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.dekaulitz.mockyup.server.utils.JsonMapper;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class ArrayNodeToArrayObjectConverter implements Converter<ArrayNode, List<Object>> {

  @Override
  public List<Object> convert(ArrayNode jsonNode) {
    return JsonMapper.mapper().convertValue(jsonNode, new TypeReference<List<Object>>() {
    });
  }
}
