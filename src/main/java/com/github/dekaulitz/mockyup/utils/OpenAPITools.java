package com.github.dekaulitz.mockyup.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class OpenAPITools {

    public static String getSimpleRef(String ref) {
        if (ref.startsWith("#/components/")) {
            ref = ref.substring(ref.lastIndexOf("/") + 1);
        }
        return ref;
    }

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

    public static Schema getSchemaFromRefSchema(Schema refSchema, OpenAPI openAPI) {
        if (StringUtils.isBlank(refSchema.get$ref())) {
            return null;
        }
        final String name = getSimpleRef(refSchema.get$ref());
        return getSchemaFromName(name, openAPI);
    }
}
