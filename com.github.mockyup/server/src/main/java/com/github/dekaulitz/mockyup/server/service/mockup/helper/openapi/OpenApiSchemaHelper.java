package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.schemas.BaseSchema;
import io.swagger.v3.oas.models.media.Schema;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenApiSchemaHelper {

  public static Map<String, BaseSchema> initOpenApiComponentSchema(Map<String, Schema> schemas) {
    Map<String, BaseSchema> baseSchemaMap = new HashMap<>();
    schemas.forEach((s, schema) -> {
      BaseSchema baseSchema = new BaseSchema();
      baseSchemaMap.put(s, baseSchema);
    });
    return baseSchemaMap;
  }
}
