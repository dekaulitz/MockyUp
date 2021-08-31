package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiContentType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiExampleEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiHeaderEmbedded;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class OpenApiCommonHelper {

  /*
   * @TODO definitions is not finished yet
   */
  protected static Map<String, OpenApiHeaderEmbedded> initOpenApiComponentHeaders(
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
          .setExamples(initOpenApiComponentExamples(header.getExamples()));
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
      /**
       * @TODO its not defined yet
       */
      openApiHeaderEmbedded.setSchema(null);
      /**
       * @TODO its not defined yet
       */
      openApiHeaderEmbedded.setContent(null);
      /**
       * @TODO its not defined yet
       */
      openApiHeaderEmbedded.setExample(null);
      openApiHeaderEmbeddedMap.put(s, openApiHeaderEmbedded);
    });
    return openApiHeaderEmbeddedMap;
  }

  /*
   * @TODO definitions is not finished yet
   */
  protected static Map<String, OpenApiExampleEmbedded> initOpenApiComponentExamples(
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
       * @TODO we can adjust the example object as response  or request on parameter,header or path variable
       */
      openApiExampleEmbedded.setValue(example.getValue());
      /*
       * @TODO need adjustment for get $ref to deep components
       */
      openApiExampleEmbedded.set$ref(example.get$ref());
      openApiExample.put(s, openApiExampleEmbedded);
    });
    return openApiExample;
  }

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
      throw new RuntimeException("invalid content format :" + content);
    }
    return contentType;
  }
}
