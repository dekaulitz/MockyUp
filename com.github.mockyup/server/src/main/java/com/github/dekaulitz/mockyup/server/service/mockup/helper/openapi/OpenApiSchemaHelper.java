package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.embedded.OpenApiDiscriminatorEmbedded;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.BaseSchema;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.OpenApiBinarySchema;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.OpenApiBooleanSchema;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.OpenApiDateSchema;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.OpenApiDateTimeSchema;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.OpenApiEmailSchema;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.OpenApiIntegerSchema;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.OpenApiNumberSchema;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.OpenApiObjectSchema;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.OpenApiPasswordSchema;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.OpenApiStringSchema;
import com.github.dekaulitz.mockyup.server.model.embeddable.document.openapi.schemas.OpenApiUUIDSchema;
import io.swagger.v3.oas.models.media.Discriminator;
import io.swagger.v3.oas.models.media.Schema;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class OpenApiSchemaHelper {

  public static final String INTEGER_TYPE = "integer";
  public static final String NUMBER_TYPE = "number";
  public static final String STRING_TYPE = "string";
  public static final String BOOLEAN_TYPE = "boolean";
  public static final String OBJECT_TYPE = "object";

  public static final String INTEGER32_FORMAT = "int32";
  public static final String INTEGER64_FORMAT = "int64";
  public static final String FLOAT_FORMAT = "float";
  public static final String DOUBLE_FORMAT = "double";
  public static final String BYTE_FORMAT = "byte";
  public static final String BINARY_FORMAT = "binary";
  public static final String DATE_FORMAT = "date";
  public static final String DATE_TIME_FORMAT = "date-time";
  public static final String PASSWORD_FORMAT = "password";
  public static final String EMAIL_FORMAT = "email";
  public static final String UUID_FORMAT = "uuid";
  private static final String TYPE = "type";
  private static final String FORMAT = "format";

  public static Map<String, BaseSchema> initOpenApiComponentSchema(Map<String, Schema> schemas) {
    Map<String, BaseSchema> baseSchemaMap = new HashMap<>();
    if (MapUtils.isEmpty(schemas)) {
      return baseSchemaMap;
    }
    schemas.forEach((s, schema) -> {
      if (StringUtils.isEmpty(s) || schema == null) {
        return;
      }
      BaseSchema baseSchema = convertSchema(schema);
      baseSchemaMap.put(s, baseSchema);
    });
    return baseSchemaMap;
  }

  public static BaseSchema createSchemaByType(ObjectNode node) {
    if (node == null) {
      return new BaseSchema();
    }
    final String type = getNodeValue(node, TYPE);
    if (StringUtils.isBlank(type)) {
      return new BaseSchema();
    }
    final String format = getNodeValue(node, FORMAT);

    return createSchema(type, format);
  }

  public static BaseSchema createSchema(String type, String format) {
    BaseSchema schema = null;
    if (INTEGER_TYPE.equals(type)) {
      if (StringUtils.isBlank(format)) {
        schema = new OpenApiIntegerSchema();
      } else {
        schema = new OpenApiIntegerSchema();
        schema.setFormat(format);
      }
    } else if (NUMBER_TYPE.equals(type)) {
      if (StringUtils.isBlank(format)) {
        schema = new OpenApiNumberSchema();
      } else {
        schema = new OpenApiNumberSchema();
        schema.setFormat(format);
      }
    } else if (BOOLEAN_TYPE.equals(type)) {
      if (StringUtils.isBlank(format)) {
        schema = new OpenApiBooleanSchema();
      } else {
        schema = new OpenApiBooleanSchema();
        schema.setFormat(format);
      }
    } else if (STRING_TYPE.equals(type)) {
      if (BYTE_FORMAT.equals(format)) {
        schema = new OpenApiBinarySchema();
        schema.setType(STRING_TYPE);
        schema.setFormat(BYTE_FORMAT);
      } else if (BINARY_FORMAT.equals(format)) {
        schema = new OpenApiBinarySchema();
        schema.setType(STRING_TYPE);
        schema.setFormat(BINARY_FORMAT);
      } else if (DATE_FORMAT.equals(format)) {
        schema = new OpenApiDateSchema();
        schema.setType(STRING_TYPE);
        schema.setFormat(DATE_FORMAT);
      } else if (DATE_TIME_FORMAT.equals(format)) {
        schema = new OpenApiDateTimeSchema();
        schema.setType(STRING_TYPE);
        schema.setFormat(DATE_TIME_FORMAT);
      } else if (PASSWORD_FORMAT.equals(format)) {
        schema = new OpenApiPasswordSchema();
        schema.setType(STRING_TYPE);
        schema.setFormat(PASSWORD_FORMAT);
      } else if (EMAIL_FORMAT.equals(format)) {
        schema = new OpenApiEmailSchema();
        schema.setType(STRING_TYPE);
        schema.setFormat(EMAIL_FORMAT);
      } else if (UUID_FORMAT.equals(format)) {
        schema = new OpenApiUUIDSchema();
        schema.setType(STRING_TYPE);
        schema.setFormat(UUID_FORMAT);
      } else {
        if (StringUtils.isBlank(format)) {
          schema = new OpenApiStringSchema();
        } else {
          schema = new OpenApiStringSchema();
          schema.setFormat(format);
        }
      }
    } else if (OBJECT_TYPE.equals(type)) {
      schema = new OpenApiObjectSchema();
    } else {
      schema = new BaseSchema();
    }
    return schema;
  }

  public static BaseSchema convertSchema(Schema schema) {
    if (schema == null) {
      return null;
    }
    BaseSchema baseSchema = createSchema(schema.getType(), schema.getFormat());
    baseSchema.setFormat(schema.getFormat());
    baseSchema.setType(schema.getType());
    baseSchema.setName(schema.getName());
    baseSchema.setTitle(schema.getTitle());
    baseSchema.setMultipleOf(schema.getMultipleOf());
    baseSchema.setMaximum(schema.getMaximum());
    baseSchema.setExclusiveMaximum(schema.getExclusiveMaximum());
    baseSchema.setMinimum(schema.getMinimum());
    baseSchema.setExclusiveMinimum(schema.getExclusiveMinimum());
    baseSchema.setMaxLength(schema.getMaxLength());
    baseSchema.setMinLength(schema.getMinLength());
    baseSchema.setPattern(schema.getPattern());
    baseSchema.setMinItems(schema.getMinItems());
    baseSchema.setMaxItems(schema.getMaxItems());
    baseSchema.setUniqueItems(schema.getUniqueItems());
    baseSchema.setMinProperties(schema.getMinProperties());
    baseSchema.setRequired(schema.getRequired());
    baseSchema.setNot(convertSchema(schema.getNot()));
    baseSchema.setProperties(initOpenApiComponentSchema(schema.getProperties()));
    baseSchema.setAdditionalProperties(schema.getAdditionalProperties());
    baseSchema.setDescription(schema.getDescription());
    baseSchema.set$ref(schema.get$ref());
    baseSchema.setNullable(schema.getNullable());
    baseSchema.setReadOnly(schema.getReadOnly());
    baseSchema.setWriteOnly(schema.getWriteOnly());
    baseSchema.setExample(schema.getExample());
    baseSchema.setExternalDocs(schema.getExternalDocs());
    baseSchema.setDeprecated(schema.getDeprecated());
    baseSchema.setXml(OpenApiXmlHelper.initOpenApiXml(schema.getXml()));
    baseSchema.setExtensions(schema.getExtensions());
    baseSchema.set_enum(schema.getEnum());
    baseSchema.setDiscriminator(initOpenApiDiscriminator(schema.getDiscriminator()));
    baseSchema.setValue(initOpenApiDiscriminator(schema.getDiscriminator()));
    return baseSchema;
  }

  private static OpenApiDiscriminatorEmbedded initOpenApiDiscriminator(
      Discriminator discriminator) {
    if (discriminator == null) {
      return null;
    }
    OpenApiDiscriminatorEmbedded openApiDiscriminatorEmbedded = new OpenApiDiscriminatorEmbedded();
    openApiDiscriminatorEmbedded.setMapping(discriminator.getMapping());
    openApiDiscriminatorEmbedded.setPropertyName(discriminator.getPropertyName());
    return openApiDiscriminatorEmbedded;
  }

  private static String getNodeValue(ObjectNode node, String field) {
    final JsonNode jsonNode = node.get(field);
    if (jsonNode == null) {
      return null;
    }
    return jsonNode.textValue();
  }
}
