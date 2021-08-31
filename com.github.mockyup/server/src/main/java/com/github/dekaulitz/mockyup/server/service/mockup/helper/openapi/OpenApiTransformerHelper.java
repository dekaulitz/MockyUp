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
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiSecurityInType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiSecurityType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiCallbackEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiContentEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiEncodingEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiLinkEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiOauthFlowEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiOauthFlowsEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiParameterEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiPathItemEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiPathResponseEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiRequestBodyEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiSecuritySchemaEmbedded;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Encoding;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
    if (CollectionUtils.isEmpty(servers)) {
      return null;
    }
    List<OpenApiServerEmbedded> openApiServerEmbeddedSet = new ArrayList<>();
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
    if (CollectionUtils.isEmpty(tags)) {
      return null;
    }
    Set<OpenApiTagEmbedded> tagEmbeddedSet = new HashSet<>();
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

  public static List<OpenApiPathEmbedded> initOpenApiPath(Paths paths) {
    if (MapUtils.isEmpty(paths)) {
      return null;
    }
    List<OpenApiPathEmbedded> openApiPathEmbeddedList = new ArrayList<>();
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
    if (CollectionUtils.isEmpty(openApiPathEmbeddedList)) {
      return;
    }
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

    List<OpenApiPathResponseEmbedded> responses = new ArrayList<>();
    initOpenApiResponse(responses, operation);
    openApiPath.setResponses(responses);
    return openApiPath;
  }

  /**
   * @TODO definitions is not finished yet
   */
  private static void initOpenApiResponse(List<OpenApiPathResponseEmbedded> responses,
      Operation operation) {
    operation.getResponses().forEach((statusCode, apiResponse) -> {
      if (StringUtils.isEmpty(statusCode) || apiResponse == null) {
        return;
      }
      /*
      @TODO take concern for this we can use statusCode as map key for easy implementation
       */
      OpenApiPathResponseEmbedded response = new OpenApiPathResponseEmbedded();
      response.setStatusCode(Integer.parseInt(statusCode));
      responses.add(response);
    });
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
    openApiComponents
        .setRequestBodies(initOpenApiComponentRequesBody(components.getRequestBodies()));
    openApiComponents
        .setSecuritySchemes(initOpenApiComponentSecuritySchemas(components.getSecuritySchemes()));
    /**
     * @TODO reference schema is not defined yet need to rerun for injecting ref schema
     */
    openApiComponents
        .setSchemas(OpenApiSchemaHelper.initOpenApiComponentSchema(components.getSchemas()));
    /**
     * @TODO its not defined yet
     */
    openApiComponents.setCallbacks(initOpenApiComponentCallBacks(components.getCallbacks()));
    return openApiComponents;
  }

  /**
   * @TODO its not defined yet should fixing the pathItem first
   */
  private static Map<String, OpenApiCallbackEmbedded> initOpenApiComponentCallBacks(
      Map<String, Callback> callbacks) {
    if (MapUtils.isEmpty(callbacks)) {
      return null;
    }
    Map<String, OpenApiCallbackEmbedded> callbackMapped = new HashMap<>();
    callbacks.forEach((s, callback) -> {
      if (StringUtils.isEmpty(s) || MapUtils.isEmpty(callback)) {
        return;
      }
      OpenApiCallbackEmbedded openApiCallbackEmbedded = new OpenApiCallbackEmbedded();
      openApiCallbackEmbedded.set$ref(callback.get$ref());
      openApiCallbackEmbedded.setExtensions(callback.getExtensions());
      List<OpenApiPathItemEmbedded> openApiPathItemEmbeddedList = new ArrayList<>();
      callback.forEach((path, pathItem) -> {
        if (StringUtils.isEmpty(path) || pathItem == null) {
          return;
        }
        OpenApiPathItemEmbedded openApiPathItemEmbedded = new OpenApiPathItemEmbedded();
        openApiPathItemEmbeddedList.add(openApiPathItemEmbedded);
      });
      openApiCallbackEmbedded.setPaths(openApiPathItemEmbeddedList);
      callbackMapped.put(s, openApiCallbackEmbedded);
    });
    return callbackMapped;
  }

  private static Map<String, OpenApiSecuritySchemaEmbedded> initOpenApiComponentSecuritySchemas(
      Map<String, SecurityScheme> securitySchemes) {
    if (MapUtils.isEmpty(securitySchemes)) {
      return null;
    }
    Map<String, OpenApiSecuritySchemaEmbedded> openApiSecuritySchemaEmbeddedMap = new HashMap<>();
    securitySchemes.forEach((s, securityScheme) -> {
      if (StringUtils.isEmpty(s) || securityScheme == null) {
        return;
      }
      OpenApiSecuritySchemaEmbedded openApiSecuritySchemaEmbedded = new OpenApiSecuritySchemaEmbedded();
      openApiSecuritySchemaEmbedded.setBearerFormat(securityScheme.getBearerFormat());
      openApiSecuritySchemaEmbedded.setScheme(securityScheme.getScheme());
      openApiSecuritySchemaEmbedded.setDescription(securityScheme.getDescription());
      openApiSecuritySchemaEmbedded.setName(securityScheme.getName());
      openApiSecuritySchemaEmbedded.set$ref(securityScheme.get$ref());
      openApiSecuritySchemaEmbedded.setOpenIdConnectUrl(securityScheme.getOpenIdConnectUrl());
      openApiSecuritySchemaEmbedded
          .setFlows(initOpenApiComponentSecurityFlows(securityScheme.getFlows()));
      String securityIn = securityScheme.getIn() == null ? null : securityScheme.getIn().toString();
      if (EnumUtils.isValidEnum(OpenApiSecurityInType.class, securityIn)) {
        openApiSecuritySchemaEmbedded
            .setIn(EnumUtils.getEnum(OpenApiSecurityInType.class, securityIn));
      }
      String securityType = securityScheme.getType() == null ? null
          : securityScheme.getType().toString();
      if (EnumUtils.isValidEnum(OpenApiSecurityType.class, securityType)) {
        openApiSecuritySchemaEmbedded
            .setType(EnumUtils.getEnum(OpenApiSecurityType.class, securityType));
      }
      /**
       * @TODO we can do adjustment for next development
       */
      openApiSecuritySchemaEmbedded.setExtensions(securityScheme.getExtensions());
      openApiSecuritySchemaEmbeddedMap.put(s, openApiSecuritySchemaEmbedded);
    });
    return openApiSecuritySchemaEmbeddedMap;
  }

  private static OpenApiOauthFlowsEmbedded initOpenApiComponentSecurityFlows(OAuthFlows flows) {
    if (flows == null) {
      return null;
    }
    OpenApiOauthFlowsEmbedded openApiOauthFlowsEmbedded = new OpenApiOauthFlowsEmbedded();
    openApiOauthFlowsEmbedded.setExtensions(flows.getExtensions());
    openApiOauthFlowsEmbedded
        .setAuthorizationCode(initOpenApiComponentSecurityFlow(flows.getAuthorizationCode()));
    openApiOauthFlowsEmbedded
        .setImplicit(initOpenApiComponentSecurityFlow(flows.getImplicit()));
    openApiOauthFlowsEmbedded
        .setPassword(initOpenApiComponentSecurityFlow(flows.getPassword()));
    openApiOauthFlowsEmbedded
        .setClientCredentials(initOpenApiComponentSecurityFlow(flows.getClientCredentials()));
    return openApiOauthFlowsEmbedded;
  }

  private static OpenApiOauthFlowEmbedded initOpenApiComponentSecurityFlow(
      OAuthFlow oAuthFlow) {
    if (oAuthFlow == null) {
      return null;
    }
    OpenApiOauthFlowEmbedded openApiOauthFlowEmbedded = new OpenApiOauthFlowEmbedded();
    openApiOauthFlowEmbedded.setScopes(oAuthFlow.getScopes());
    openApiOauthFlowEmbedded.setExtensions(oAuthFlow.getExtensions());
    openApiOauthFlowEmbedded.setRefreshUrl(oAuthFlow.getRefreshUrl());
    openApiOauthFlowEmbedded.setTokenUrl(oAuthFlow.getTokenUrl());
    openApiOauthFlowEmbedded.setAuthorizationUrl(oAuthFlow.getAuthorizationUrl());
    return openApiOauthFlowEmbedded;
  }

  private static Map<String, OpenApiRequestBodyEmbedded> initOpenApiComponentRequesBody(
      Map<String, RequestBody> requestBodies) {
    if (MapUtils.isEmpty(requestBodies)) {
      return null;
    }
    Map<String, OpenApiRequestBodyEmbedded> openApiRequestBodyEmbeddedMap = new HashMap<>();
    requestBodies.forEach((s, requestBody) -> {
      if (StringUtils.isEmpty(s) || requestBody == null) {
        return;
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
      openApiRequestBodyEmbeddedMap.put(s, openApiRequestBodyEmbedded);
    });
    return openApiRequestBodyEmbeddedMap;
  }

  /**
   * @TODO definitions is not finished yet
   */
  private static List<OpenApiPathResponseEmbedded> initOpenApiComponentResponse(
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

  /**
   * @TODO definitions is not finished yet
   */
  private static Map<String, OpenApiLinkEmbedded> initOpenApiComponentLinks(
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
    if (MapUtils.isEmpty(parameters)) {
      return null;
    }
    Map<String, OpenApiParameterEmbedded> openApiParameterEmbeddedMap = new HashMap<>();
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
}
