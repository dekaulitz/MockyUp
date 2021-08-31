package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiEncodingType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiContentEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiEncodingEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiLinkEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiPathResponseEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiRequestBodyEmbedded;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

public class OpenApiPayloadHelper {

  protected static OpenApiRequestBodyEmbedded getOpenApiRequestBody(RequestBody requestBody) {
    if (requestBody == null) {
      return null;
    }
    OpenApiRequestBodyEmbedded openApiRequestBodyEmbedded = new OpenApiRequestBodyEmbedded();
    openApiRequestBodyEmbedded.setContent(initOpenApiComponentContent(requestBody.getContent()));
    openApiRequestBodyEmbedded.set$ref(requestBody.get$ref());
    openApiRequestBodyEmbedded.setDescription(requestBody.getDescription());
    openApiRequestBodyEmbedded.setRequired(requestBody.getRequired());
    /**
     * @TODO we can do adjustment for future development
     */
    openApiRequestBodyEmbedded.setExtensions(requestBody.getExtensions());
    return openApiRequestBodyEmbedded;
  }

  /**
   * @TODO definitions is not finished yet
   */
  protected static List<OpenApiContentEmbedded> initOpenApiComponentContent(Content content) {
    if (MapUtils.isEmpty(content)) {
      return null;
    }
    List<OpenApiContentEmbedded> openApiContentEmbeddedList = new ArrayList<>();
    content.forEach((s, mediaType) -> {
      OpenApiContentEmbedded openApiContentEmbedded = new OpenApiContentEmbedded();
      openApiContentEmbedded.setContentType(OpenApiCommonHelper.getContentType(s));
      openApiContentEmbedded
          .setExamples(OpenApiCommonHelper.initOpenApiComponentExamples(mediaType.getExamples()));
      openApiContentEmbedded.setEncoding(initOpenApiComponentEncoding(mediaType.getEncoding()));
      openApiContentEmbedded.setExample(mediaType.getExample());
      openApiContentEmbedded.setSchema(OpenApiSchemaHelper.convertSchema(mediaType.getSchema()));
      /**
       * @TODO we can do adjustment for kafka publish message or something else
       */
      openApiContentEmbedded.setExtensions(mediaType.getExtensions());
      openApiContentEmbeddedList.add(openApiContentEmbedded);
    });
    return openApiContentEmbeddedList;
  }

  /**
   * @TODO definitions is not finished yet
   */
  protected static List<OpenApiPathResponseEmbedded> getOpenApiComponentResponse(
      Map<String, ApiResponse> responses) {
    if (MapUtils.isEmpty(responses)) {
      return null;
    }
    List<OpenApiPathResponseEmbedded> openApiPathResponses = new ArrayList<>();
    responses.forEach((s, apiResponse) -> {
      if (StringUtils.isEmpty(s) || apiResponse == null) {
        return;
      }
      OpenApiPathResponseEmbedded openApiPathResponse = new OpenApiPathResponseEmbedded();
      openApiPathResponse.setDescription(apiResponse.getDescription());
      openApiPathResponse.setStatusCode(Integer.parseInt(s));
      openApiPathResponse
          .setHeaders(OpenApiCommonHelper.initOpenApiComponentHeaders(apiResponse.getHeaders()));
      openApiPathResponse.set$ref(apiResponse.get$ref());
      openApiPathResponse.setLinks(getOpenApiComponentLinks(apiResponse.getLinks()));
      openApiPathResponse.setContent(initOpenApiComponentContent(apiResponse.getContent()));
      /*
       * @TODO we can do adjustment for future features
       */
      openApiPathResponse.setExtensions(apiResponse.getExtensions());
      openApiPathResponses.add(openApiPathResponse);
    });
    return openApiPathResponses;
  }

  /**
   * @TODO definitions is not finished yet
   */
  protected static Map<String, OpenApiLinkEmbedded> getOpenApiComponentLinks(
      Map<String, Link> links) {
    if (MapUtils.isEmpty(links)) {
      return null;
    }
    Map<String, OpenApiLinkEmbedded> openApiLinkEmbeddedMap = new HashMap<>();
    links.forEach((s, link) -> {
      if (StringUtils.isEmpty(s) || link == null) {
        return;
      }
      OpenApiLinkEmbedded openApiLinkEmbedded = new OpenApiLinkEmbedded();
      openApiLinkEmbedded.setOperationId(link.getOperationId());
      openApiLinkEmbedded.setOperationRef(link.getOperationRef());
      openApiLinkEmbedded.setParameters(link.getParameters());
      openApiLinkEmbedded.setRequestBody(link.getRequestBody());
      openApiLinkEmbedded
          .setHeaders(OpenApiCommonHelper.initOpenApiComponentHeaders(link.getHeaders()));
      openApiLinkEmbedded.setDescription(link.getDescription());
      openApiLinkEmbedded.set$ref(link.get$ref());
      openApiLinkEmbedded.setExtensions(link.getExtensions());
      openApiLinkEmbedded.setServer(OpenApiServerHelper.getOpenApiServers(link.getServer()));
      openApiLinkEmbeddedMap.put(s, openApiLinkEmbedded);
    });
    return openApiLinkEmbeddedMap;
  }

  private static Map<String, OpenApiEncodingEmbedded> initOpenApiComponentEncoding(
      Map<String, Encoding> encodings) {
    if (MapUtils.isEmpty(encodings)) {
      return null;
    }
    Map<String, OpenApiEncodingEmbedded> openApiEncodingEmbeddedMap = new HashMap<>();
    encodings.forEach((s, encoding) -> {
      OpenApiEncodingEmbedded openApiEncodingEmbedded = new OpenApiEncodingEmbedded();
      // @TODO we can use constat for avoiding supporting content type
      openApiEncodingEmbedded.setContentType(encoding.getContentType());
      openApiEncodingEmbedded
          .setHeaders(OpenApiCommonHelper.initOpenApiComponentHeaders(encoding.getHeaders()));
      String style = encoding.getStyle() == null ? null : encoding.getStyle().toString();
      if (EnumUtils.isValidEnum(OpenApiEncodingType.class, style)) {
        openApiEncodingEmbedded.setStyle(EnumUtils.getEnum(OpenApiEncodingType.class, style));
      }
      openApiEncodingEmbedded.setExplode(encoding.getExplode());
      openApiEncodingEmbedded.setAllowReserved(encoding.getAllowReserved());
      // @TODO we adjustment for next future development
      openApiEncodingEmbedded.setExtensions(encoding.getExtensions());
      openApiEncodingEmbeddedMap.put(s, openApiEncodingEmbedded);
    });
    return openApiEncodingEmbeddedMap;
  }

}
