package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiContentType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiExampleEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiHeaderEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiSecurityEmbedded;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * common function that help for transforming OpenApi model into our entities
 *
 * @see {@link io.swagger.v3.oas.models.OpenAPI}
 */
public class OpenApiCommonHelper {

  /**
   * transforming from {@link Header}
   *
   * @see {@link OpenApiHeaderEmbedded}
   */
  protected static Map<String, OpenApiHeaderEmbedded> getOpenApiComponentHeader(
      Map<String, Header> headers) {
    if (MapUtils.isEmpty(headers)) {
      return null;
    }
    Map<String, OpenApiHeaderEmbedded> openApiHeaderEmbeddedMap = new HashMap<>();
    headers.forEach((s, header) -> {
      if (StringUtils.isEmpty(s) || header == null) {
        return;
      }
      OpenApiHeaderEmbedded openApiHeaderEmbedded = new OpenApiHeaderEmbedded();
      openApiHeaderEmbedded.set$ref(header.get$ref());
      openApiHeaderEmbedded
          .setExamples(getOpenApiComponentExample(header.getExamples()));
      openApiHeaderEmbedded.setDescription(header.getDescription());
      openApiHeaderEmbedded.setRequired(header.getRequired());
      openApiHeaderEmbedded.setDeprecated(header.getDeprecated());
      String headerStyle = header.getStyle() == null ? null
          : header.getStyle().toString();
      if (EnumUtils.isValidEnum(OpenApiHeaderEmbedded.StyleEnum.class, headerStyle)) {
        openApiHeaderEmbedded
            .setStyle(EnumUtils.getEnum(OpenApiHeaderEmbedded.StyleEnum.class, headerStyle));
      }
      openApiHeaderEmbedded.setExplode(header.getExplode());
      openApiHeaderEmbedded.setExtensions(header.getExtensions());
      openApiHeaderEmbedded.setSchema(OpenApiSchemaHelper.convertSchema(header.getSchema()));
      openApiHeaderEmbedded
          .setContent(OpenApiPayloadHelper.initOpenApiComponentContent(header.getContent()));
      openApiHeaderEmbedded.setExample(header.getExample());
      openApiHeaderEmbeddedMap.put(s, openApiHeaderEmbedded);
    });
    return openApiHeaderEmbeddedMap;
  }

  /**
   * get component examples
   */
  protected static Map<String, OpenApiExampleEmbedded> getOpenApiComponentExample(
      Map<String, Example> examples) {
    if (MapUtils.isEmpty(examples)) {
      return null;
    }
    Map<String, OpenApiExampleEmbedded> openApiExample = new HashMap<>();
    examples.forEach((s, example) -> {
      if (StringUtils.isEmpty(s) || example == null) {
        return;
      }
      OpenApiExampleEmbedded openApiExampleEmbedded = new OpenApiExampleEmbedded();
      openApiExampleEmbedded.setDescription(example.getDescription());
      openApiExampleEmbedded.setExtensions(example.getExtensions());
      openApiExampleEmbedded.setExternalValue(example.getExternalValue());
      openApiExampleEmbedded.setSummary(example.getSummary());
      /*
       * @TODO we use the example object as default response  or request on parameter,header or path variable
       */
      openApiExampleEmbedded.setValue(example.getValue());
      openApiExampleEmbedded.set$ref(example.get$ref());
      openApiExample.put(s, openApiExampleEmbedded);
    });
    return openApiExample;
  }

  /**
   * parsing content type into supporting content type for limiting the supported content type
   *
   * @see {@link OpenApiContentType} for supporting content type
   */
  public static OpenApiContentType getContentType(String content) {
    Map<String, OpenApiContentType> contentMap = EnumUtils
        .getEnumMap(OpenApiContentType.class);
    OpenApiContentType contentType = null;
    for (Entry<String, OpenApiContentType> entry : contentMap.entrySet()) {
      OpenApiContentType openApiContentType = entry.getValue();
      if (openApiContentType.getValue().equalsIgnoreCase(content)) {
        contentType = openApiContentType;
        break;
      }
    }
    if (contentType == null) {
      throw new RuntimeException("invalid content format or not supported yet : " + content);
    }
    return contentType;
  }

  protected static void initSecurityRequirement(SecurityRequirement securityRequirement,
      List<OpenApiSecurityEmbedded> securityList) {
    if (MapUtils.isNotEmpty(securityRequirement)) {
      securityRequirement.forEach((s, strings) -> {
        OpenApiSecurityEmbedded openApiSecurityEmbedded = new OpenApiSecurityEmbedded();
        if (CollectionUtils.isNotEmpty(strings)) {
          openApiSecurityEmbedded.addList(s, strings);
        } else {
          openApiSecurityEmbedded.addList(s);
        }
        securityList.add(openApiSecurityEmbedded);
      });

    }
  }
}
