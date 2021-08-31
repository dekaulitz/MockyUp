package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiComponents;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.schemas.BaseSchema;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenApiRefHelper {

  public final static String SCHEMAS = "schemas";
  public final static String PARAMETERS = "parameters";
  public final static String EXAMPLES = "examples";
  public final static String HEADERS = "headers";
  public final static String RESPONSES = "responses";

  public static String getSimpleRef(String ref) {
    if (ref.startsWith("#/components/")) {
      ref = ref.substring(ref.lastIndexOf("/") + 1);
    }
    return ref;
  }

  public static BaseSchema getSchemaFromName(String name, OpenApiComponents openApiComponents) {
    if (openApiComponents == null) {
      return null;
    }
    final Map<String, BaseSchema> mapSchema = openApiComponents.getSchemas();
    if (mapSchema == null || mapSchema.isEmpty() || mapSchema.containsKey(name)) {
      throw new RuntimeException("getSchemaFromName schema not found : " + name);
    }
    return mapSchema.get(name);
  }
}
