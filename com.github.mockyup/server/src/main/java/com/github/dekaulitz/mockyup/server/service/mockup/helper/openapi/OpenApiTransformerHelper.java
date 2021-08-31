package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiComponents;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiPathEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiProjectInfoEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiServerEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiTagEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiEncodingType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiParameterPosition;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiParameterStyle;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiPathHttpMethod;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiContentEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiEncodingEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiLinkEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiParameterEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiPathResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;

@Slf4j
public class OpenApiTransformerHelper {

  public static OpenApiProjectInfoEmbedded getOpenApiInfo(Info info, ModelMapper modelMapper) {
    return modelMapper
        .map(info, OpenApiProjectInfoEmbedded.class);
  }

  public static List<OpenApiServerEmbedded> getOpenApiServers(List<Server> servers) {
    List<OpenApiServerEmbedded> openApiServerEmbeddedSet = new ArrayList<>();
    if (CollectionUtils.isEmpty(servers)) {
      return openApiServerEmbeddedSet;
    }
    servers.forEach(server -> {
      // cannot use model mapper issue on variables when mapping
      OpenApiServerEmbedded openApiServerEmbedded = new OpenApiServerEmbedded();
      openApiServerEmbedded.setUrl(server.getUrl());
      openApiServerEmbedded.setDescription(server.getDescription());
      openApiServerEmbedded.setVariables(server.getVariables());
      openApiServerEmbedded.setExtensions(server.getExtensions());
      openApiServerEmbeddedSet.add(openApiServerEmbedded);
    });
    return openApiServerEmbeddedSet;
  }

  public static Set<OpenApiTagEmbedded> getOpenApiTags(List<Tag> tags) {
    Set<OpenApiTagEmbedded> tagEmbeddedSet = new HashSet<>();
    if (CollectionUtils.isEmpty(tags)) {
      return tagEmbeddedSet;
    }
    tags.forEach(tag -> {
      OpenApiTagEmbedded openApiTagEmbedded = new OpenApiTagEmbedded();
      openApiTagEmbedded.setName(tag.getName());
      openApiTagEmbedded.setExternalDocs(tag.getExternalDocs());
      openApiTagEmbedded.setDescription(tag.getDescription());
      openApiTagEmbedded.setExtensions(tag.getExtensions());
      tagEmbeddedSet.add(openApiTagEmbedded);
    });
    return tagEmbeddedSet;
  }

  /*
   * @TODO definitions is not finished yet
   */
  public static OpenApiComponents getOpenApiComponents(Components components) {
    OpenApiComponents openApiComponents = new OpenApiComponents();
    /*
     * @TODO we can adjust the extension for generate some property related with server information
     */
    openApiComponents.setExtensions(components.getExtensions());
    openApiComponents
        .setExamples(OpenApiCommonHelper.initOpenApiComponentExamples(components.getExamples()));
    openApiComponents
        .setHeaders(OpenApiCommonHelper.initOpenApiComponentHeaders(components.getHeaders()));
    openApiComponents.setParameters(initOpenApiComponentParameters(components.getParameters()));
    openApiComponents.setLinks(initOpenApiComponentLinks(components.getLinks()));
    openApiComponents.setResponses(initOpenApiComponentResponse(components.getResponses()));
    /**
     * @TODO its not defined yet
     */
    openApiComponents
        .setSchemas(OpenApiSchemaHelper.initOpenApiComponentSchema(components.getSchemas()));
    /**
     * @TODO its not defined yet
     */
    openApiComponents.setRequestBodies(null);
    /**
     * @TODO its not defined yet
     */
    openApiComponents.setSecuritySchemes(null);
    /**
     * @TODO its not defined yet
     */
    openApiComponents.setCallbacks(null);
    return openApiComponents;
  }

  /**
   * @TODO definitions is not finished yet
   */
  private static List<OpenApiPathResponse> initOpenApiComponentResponse(
      Map<String, ApiResponse> responses) {
    List<OpenApiPathResponse> openApiPathResponses = new ArrayList<>();
    if (MapUtils.isEmpty(responses)) {
      return openApiPathResponses;
    }
    responses.forEach((s, apiResponse) -> {
      if (StringUtils.isEmpty(s) || apiResponse == null) {
        return;
      }
      OpenApiPathResponse openApiPathResponse = new OpenApiPathResponse();
      openApiPathResponse.setDescription(apiResponse.getDescription());
      openApiPathResponse.setStatusCode(Integer.parseInt(s));
      openApiPathResponse
          .setHeaders(OpenApiCommonHelper.initOpenApiComponentHeaders(apiResponse.getHeaders()));
      openApiPathResponse.set$ref(apiResponse.get$ref());
      openApiPathResponse.setLinks(initOpenApiComponentLinks(apiResponse.getLinks()));
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
  private static List<OpenApiContentEmbedded> initOpenApiComponentContent(Content content) {
    List<OpenApiContentEmbedded> openApiContentEmbeddedList = new ArrayList<>();
    if (MapUtils.isEmpty(content)) {
      return openApiContentEmbeddedList;
    }
    content.forEach((s, mediaType) -> {
      OpenApiContentEmbedded openApiContentEmbedded = new OpenApiContentEmbedded();
      openApiContentEmbedded.setContentType(OpenApiCommonHelper.getContentType(s));
      openApiContentEmbedded
          .setExamples(OpenApiCommonHelper.initOpenApiComponentExamples(mediaType.getExamples()));
      openApiContentEmbedded.setEncoding(initOpenApiComponentEncoding(mediaType.getEncoding()));
      openApiContentEmbedded.setExample(mediaType.getExample());
      /**
       * @TODO its not defined yet
       */
      openApiContentEmbedded.setSchema(null);
      /**
       * @TODO we can do adjustment for kafka publish message or something else
       */
      openApiContentEmbedded.setExtensions(mediaType.getExtensions());
      openApiContentEmbeddedList.add(openApiContentEmbedded);
    });
    return openApiContentEmbeddedList;
  }

  private static Map<String, OpenApiEncodingEmbedded> initOpenApiComponentEncoding(
      Map<String, Encoding> encodings) {
    Map<String, OpenApiEncodingEmbedded> openApiEncodingEmbeddedMap = new HashMap<>();
    if (MapUtils.isEmpty(encodings)) {
      return openApiEncodingEmbeddedMap;
    }
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

  /**
   * @TODO definitions is not finished yet
   */
  private static Map<String, OpenApiLinkEmbedded> initOpenApiComponentLinks(
      Map<String, Link> links) {
    Map<String, OpenApiLinkEmbedded> openApiLinkEmbeddedMap = new HashMap<>();
    if (MapUtils.isEmpty(links)) {
      return openApiLinkEmbeddedMap;
    }
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
      List<Server> servers = Collections.singletonList(link.getServer());
      if (CollectionUtils.isNotEmpty(servers)) {
        List<OpenApiServerEmbedded> openApiServers = getOpenApiServers(servers);
        OpenApiServerEmbedded openApiServerEmbedded =
            CollectionUtils.isEmpty(openApiServers) ? null : openApiServers.get(0);
        openApiLinkEmbedded.setServer(openApiServerEmbedded);
      }
      openApiLinkEmbeddedMap.put(s, openApiLinkEmbedded);
    });
    return openApiLinkEmbeddedMap;
  }


  /**
   * @TODO definitions is not finished yet
   */
  private static Map<String, OpenApiParameterEmbedded> initOpenApiComponentParameters(
      Map<String, Parameter> parameters) {
    Map<String, OpenApiParameterEmbedded> openApiParameterEmbeddedMap = new HashMap<>();
    if (MapUtils.isEmpty(parameters)) {
      return openApiParameterEmbeddedMap;
    }
    parameters.forEach((s, parameter) -> {
      if (StringUtils.isEmpty(s) || parameter == null) {
        return;
      }
      OpenApiParameterEmbedded openApiParameterEmbedded = new OpenApiParameterEmbedded();
      String parameterPosition = parameter.getIn() == null ? null : parameter.getIn().toUpperCase();
      if (EnumUtils.isValidEnum(OpenApiParameterPosition.class, parameterPosition)) {
        openApiParameterEmbedded
            .setIn(EnumUtils.getEnum(OpenApiParameterPosition.class, parameterPosition));
      }
      String parameterStyle = parameter.getStyle() == null ? null
          : parameter.getStyle().toString();
      if (EnumUtils.isValidEnum(OpenApiParameterStyle.class, parameterStyle)) {
        openApiParameterEmbedded
            .setStyle(EnumUtils.getEnum(OpenApiParameterStyle.class, parameterStyle));
      }
      openApiParameterEmbedded.setName(parameter.getName());
      openApiParameterEmbedded.setDescription(parameter.getDescription());
      openApiParameterEmbedded.setRequired(parameter.getRequired());
      openApiParameterEmbedded.setDeprecated(parameter.getDeprecated());
      openApiParameterEmbedded.setAllowEmptyValue(parameter.getAllowEmptyValue());
      openApiParameterEmbedded.set$ref(parameter.get$ref());
      openApiParameterEmbedded.setExplode(parameter.getExplode());
      openApiParameterEmbedded.setAllowReserved(parameter.getAllowReserved());
      openApiParameterEmbedded.setExtensions(parameter.getExtensions());
      openApiParameterEmbedded
          .setExamples(OpenApiCommonHelper.initOpenApiComponentExamples(parameter.getExamples()));

      /**
       * @TODO its not defined yet
       */
      openApiParameterEmbedded.setSchema(null);
      /**
       * @TODO its not defined yet
       */
      openApiParameterEmbedded.setExample(null);
      /**
       * @TODO its not defined yet
       */
      openApiParameterEmbedded.setContent(null);
      openApiParameterEmbeddedMap.put(s, openApiParameterEmbedded);
    });
    return openApiParameterEmbeddedMap;
  }


  public static List<OpenApiPathEmbedded> getOpenApiPaths(Paths paths) {
    List<OpenApiPathEmbedded> openApiPathEmbeddedList = new ArrayList<>();
    if (MapUtils.isEmpty(paths)) {
      return openApiPathEmbeddedList;
    }
    paths.forEach((path, pathItem) -> {
      if (StringUtils.isEmpty(path) || pathItem == null) {
        return;
      }
      getOpenApiPathInformation(path, pathItem, openApiPathEmbeddedList);

    });
    return openApiPathEmbeddedList;
  }

  private static void getOpenApiPathInformation(String path, PathItem pathItem,
      List<OpenApiPathEmbedded> openApiPathEmbeddedList) {
    initCommonHttpRequestOpenApi(path, pathItem, openApiPathEmbeddedList);
    initRareHttpRequestOpenApi(path, pathItem, openApiPathEmbeddedList);
  }

  private static void initCommonHttpRequestOpenApi(String path, PathItem pathItem,
      List<OpenApiPathEmbedded> openApiPathEmbeddedList) {
    if (pathItem.getGet() != null) {
      openApiPathEmbeddedList.add(initOpenApiHttpRequest(path, pathItem, pathItem.getGet(),
          OpenApiPathHttpMethod.GET));
    }
    if (pathItem.getDelete() != null) {
      openApiPathEmbeddedList.add(initOpenApiHttpRequest(path, pathItem, pathItem.getDelete(),
          OpenApiPathHttpMethod.DELETE));
    }
    if (pathItem.getPost() != null) {
      openApiPathEmbeddedList.add(initOpenApiHttpRequest(path, pathItem, pathItem.getPost(),
          OpenApiPathHttpMethod.POST));
    }
    if (pathItem.getPut() != null) {
      openApiPathEmbeddedList.add(initOpenApiHttpRequest(path, pathItem, pathItem.getPut(),
          OpenApiPathHttpMethod.PUT));
    }
  }

  private static void initRareHttpRequestOpenApi(String path, PathItem pathItem,
      List<OpenApiPathEmbedded> openApiPathEmbeddedList) {

    if (pathItem.getHead() != null) {
      openApiPathEmbeddedList.add(
          initOpenApiHttpRequest(path, pathItem, pathItem.getHead(), OpenApiPathHttpMethod.HEAD));
    }
    if (pathItem.getPatch() != null) {
      openApiPathEmbeddedList.add(initOpenApiHttpRequest(path, pathItem, pathItem.getPatch(),
          OpenApiPathHttpMethod.PATCH));
    }
    if (pathItem.getTrace() != null) {
      openApiPathEmbeddedList.add(initOpenApiHttpRequest(path, pathItem, pathItem.getTrace(),
          OpenApiPathHttpMethod.TRACE));
    }
    if (pathItem.getOptions() != null) {
      openApiPathEmbeddedList.add(initOpenApiHttpRequest(path, pathItem, pathItem.getOptions(),
          OpenApiPathHttpMethod.OPTIONS));
    }
  }

  /**
   * @TODO need passing component {@link io.swagger.v3.oas.models.Components} object for injecting
   * schemas or model
   */
  private static OpenApiPathEmbedded initOpenApiHttpRequest(String path, PathItem pathItem,
      Operation operation, OpenApiPathHttpMethod openApiPathHttpMethod) {
    OpenApiPathEmbedded openApiPath = new OpenApiPathEmbedded();
    openApiPath.setPath(path);
    openApiPath.setTags(operation.getTags());
    openApiPath.setHttpMethod(openApiPathHttpMethod);
    openApiPath.setDescription(operation.getDescription());
    openApiPath.setExternalDocs(operation.getExternalDocs());
    openApiPath.setSummary(operation.getSummary());
    /*
     * @TODO should enhance the extension for mocking response and request
     * define mocking request and response for make youre live easier we have'nt default definition yet on this
     */
    openApiPath.setExtensions(operation.getExtensions());
    /*
     * @TODO we can adjust the operation for codegen for next feature development
     */
    openApiPath.setOperationId(null);

    List<OpenApiPathResponse> responses = new ArrayList<>();
    initOpenApiResponse(responses, operation);
    openApiPath.setResponses(responses);
    return openApiPath;
  }

  /**
   * @TODO definitions is not finished yet
   */
  private static void initOpenApiResponse(List<OpenApiPathResponse> responses,
      Operation operation) {
    operation.getResponses().forEach((statusCode, apiResponse) -> {
      if (StringUtils.isEmpty(statusCode) || apiResponse == null) {
        return;
      }
      /*
      @TODO take concern for this we can use statusCode as map key for easy implementation
       */
      OpenApiPathResponse response = new OpenApiPathResponse();
      response.setStatusCode(Integer.parseInt(statusCode));
      responses.add(response);
    });
  }
}
