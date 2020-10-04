package com.github.dekaulitz.mockyup.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class OpenAPITools {

  /**
   * get component name from reff schema
   *
   * @param ref reff schema
   * @return String
   */
  public static String getSimpleRef(String ref) {
    if (ref.startsWith("#/components/")) {
      ref = ref.substring(ref.lastIndexOf("/") + 1);
    }
    return ref;
  }

  /**
   * get schema object base on schema name
   *
   * @param name    schema name
   * @param openAPI openapi object
   * @return Schema
   */
  public static Schema getSchemaFromName(String name, OpenAPI openAPI) {
    if (openAPI.getComponents() == null) {
      return null;
    }
    final Map<String, Schema> mapSchema = openAPI.getComponents().getSchemas();
    if (mapSchema == null || mapSchema.isEmpty()) {
      return null;
    }
    return mapSchema.get(name);
  }

  /**
   * get schma base on reff schema
   *
   * @param refSchema reffschema link
   * @param openAPI   openapi object
   * @return Schema
   */
  public static Schema getSchemaFromRefSchema(Schema refSchema, OpenAPI openAPI) {
    if (StringUtils.isBlank(refSchema.get$ref())) {
      return null;
    }
    final String name = getSimpleRef(refSchema.get$ref());
    return getSchemaFromName(name, openAPI);
  }
}
