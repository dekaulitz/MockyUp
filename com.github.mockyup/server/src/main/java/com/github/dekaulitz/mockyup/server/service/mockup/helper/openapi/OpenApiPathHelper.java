package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiPathEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiServerEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiParameterPosition;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiParameterStyle;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiPathHttpMethod;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiCallbackEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiParameterEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiPathOperationEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiSecurityEmbedded;
import com.github.dekaulitz.mockyup.server.service.mockup.helper.mockup.DevStockHelper;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @see {@link PathItem} {@link Callback} {@link Operation}
 */
public class OpenApiPathHelper {

  /**
   * @see {@link Callback} transforming callbacks into OpenApiCallbackEmbedded
   * <p>
   * {@link OpenApiCallbackEmbedded}
   */
  protected static Map<String, OpenApiCallbackEmbedded> getOpenApiComponentCallBacks(
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
      List<OpenApiPathEmbedded> openApiPathEmbeddedList = new ArrayList<>();
      callback.forEach((path, pathItem) -> {
        if (StringUtils.isEmpty(path) || pathItem == null) {
          return;
        }
        OpenApiPathHelper.getOpenApiPathInformation(path, callback.getExtensions(), pathItem,
            openApiPathEmbeddedList);
      });
      openApiCallbackEmbedded.setPaths(openApiPathEmbeddedList);
      callbackMapped.put(s, openApiCallbackEmbedded);
    });
    return callbackMapped;
  }

  /**
   * rendering request information base on the path and http method
   */
  protected static void getOpenApiPathInformation(String path, Map<String, Object> extensions,
      PathItem pathItem, List<OpenApiPathEmbedded> openApiPathEmbeddedList) {
    // common http request
    getCommonHttpRequestOpenApi(path, extensions, pathItem, openApiPathEmbeddedList);
    // rare http request
    getRareHttpRequestOpenApi(path, extensions, pathItem, openApiPathEmbeddedList);
  }

  /**
   * transforming common http request like crud base on the path
   */
  private static void getCommonHttpRequestOpenApi(String path,
      Map<String, Object> extensions, PathItem pathItem,
      List<OpenApiPathEmbedded> openApiPathEmbeddedList) {
    if (pathItem == null) {
      return;
    }
    if (pathItem.getGet() != null) {
      OpenApiPathEmbedded openApiPathEmbedded = getOpenApiHttpRequest(path, pathItem,
          pathItem.getGet(), OpenApiPathHttpMethod.GET, extensions);
      openApiPathEmbeddedList.add(openApiPathEmbedded);
    }
    if (pathItem.getDelete() != null) {
      OpenApiPathEmbedded openApiPathEmbedded = getOpenApiHttpRequest(path, pathItem,
          pathItem.getDelete(), OpenApiPathHttpMethod.DELETE, extensions);
      openApiPathEmbeddedList.add(openApiPathEmbedded);
    }
    if (pathItem.getPost() != null) {
      OpenApiPathEmbedded openApiPathEmbedded = getOpenApiHttpRequest(path, pathItem,
          pathItem.getPost(), OpenApiPathHttpMethod.POST, extensions);
      openApiPathEmbeddedList.add(openApiPathEmbedded);
    }
    if (pathItem.getPut() != null) {
      OpenApiPathEmbedded openApiPathEmbedded = getOpenApiHttpRequest(path, pathItem,
          pathItem.getPut(), OpenApiPathHttpMethod.PUT, extensions);
      openApiPathEmbeddedList.add(openApiPathEmbedded);
    }
  }

  private static void getRareHttpRequestOpenApi(String path, Map<String, Object> extensions,
      PathItem pathItem, List<OpenApiPathEmbedded> openApiPathEmbeddedList) {

    if (pathItem.getHead() != null) {
      OpenApiPathEmbedded openApiPathEmbedded = getOpenApiHttpRequest(path, pathItem,
          pathItem.getHead(), OpenApiPathHttpMethod.HEAD, extensions);
      openApiPathEmbeddedList.add(openApiPathEmbedded);
    }
    if (pathItem.getPatch() != null) {
      OpenApiPathEmbedded openApiPathEmbedded = getOpenApiHttpRequest(path, pathItem,
          pathItem.getPatch(), OpenApiPathHttpMethod.PATCH, extensions);
      openApiPathEmbeddedList.add(openApiPathEmbedded);
    }
    if (pathItem.getTrace() != null) {
      OpenApiPathEmbedded openApiPathEmbedded = getOpenApiHttpRequest(path, pathItem,
          pathItem.getTrace(), OpenApiPathHttpMethod.TRACE, extensions);
      openApiPathEmbeddedList.add(openApiPathEmbedded);
    }
    if (pathItem.getOptions() != null) {
      OpenApiPathEmbedded openApiPathEmbedded = getOpenApiHttpRequest(path, pathItem,
          pathItem.getOptions(), OpenApiPathHttpMethod.OPTIONS, extensions);
      openApiPathEmbeddedList.add(openApiPathEmbedded);
    }
  }

  protected static OpenApiParameterEmbedded getOpenApiParameter(Parameter parameter) {
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
        .setExamples(OpenApiCommonHelper.getOpenApiComponentExample(parameter.getExamples()));
    openApiParameterEmbedded.setSchema(OpenApiSchemaHelper.convertSchema(parameter.getSchema()));
    openApiParameterEmbedded.setExample(parameter.getExample());
    openApiParameterEmbedded
        .setContent(OpenApiPayloadHelper.initOpenApiComponentContent(parameter.getContent()));
    return openApiParameterEmbedded;
  }

  private static OpenApiPathEmbedded getOpenApiHttpRequest(String path, PathItem pathItem,
      Operation operation, OpenApiPathHttpMethod openApiPathHttpMethod,
      Map<String, Object> extensions) {
    OpenApiPathEmbedded openApiPathEmbedded = new OpenApiPathEmbedded();
    openApiPathEmbedded.setPath(path);
    openApiPathEmbedded.setHttpMethod(openApiPathHttpMethod);
    openApiPathEmbedded.setExtensions(extensions);
    openApiPathEmbedded.set$ref(pathItem.get$ref());
    openApiPathEmbedded.setOperation(getOpenApiPathOperation(operation));
    if (CollectionUtils.isNotEmpty(pathItem.getServers())) {
      List<OpenApiServerEmbedded> openApiServerEmbeddedList = getPathServers(
          operation.getServers());
      openApiPathEmbedded.setServers(openApiServerEmbeddedList);
    }
    if (CollectionUtils.isNotEmpty(pathItem.getParameters())) {
      List<OpenApiParameterEmbedded> parameterEmbeddedList = getPathItemParameters(
          operation.getParameters());
      openApiPathEmbedded.setParameters(parameterEmbeddedList);
    }
    DevStockHelper.setupOperationPathConfiguration(openApiPathEmbedded.getOperation());
    return openApiPathEmbedded;
  }

  private static OpenApiPathOperationEmbedded getOpenApiPathOperation(Operation operation) {
    OpenApiPathOperationEmbedded openApiPathOperationEmbedded = new OpenApiPathOperationEmbedded();
    openApiPathOperationEmbedded.setSummary(operation.getSummary());
    openApiPathOperationEmbedded.setDescription(operation.getDescription());
    if (CollectionUtils.isNotEmpty(operation.getServers())) {
      List<OpenApiServerEmbedded> openApiServerEmbeddedList = getPathServers(
          operation.getServers());
      openApiPathOperationEmbedded.setServers(openApiServerEmbeddedList);
    }
    if (CollectionUtils.isNotEmpty(operation.getParameters())) {
      List<OpenApiParameterEmbedded> parameterEmbeddedList = getPathItemParameters(
          operation.getParameters());
      openApiPathOperationEmbedded.setParameters(parameterEmbeddedList);
    }
    openApiPathOperationEmbedded.setTags(operation.getTags());
    openApiPathOperationEmbedded.setExternalDocs(operation.getExternalDocs());
    openApiPathOperationEmbedded.setExtensions(operation.getExtensions());
    openApiPathOperationEmbedded.setOperationId(operation.getOperationId());
    if (CollectionUtils.isNotEmpty(operation.getSecurity())) {
      List<OpenApiSecurityEmbedded> securityList = new ArrayList<>();
      operation.getSecurity().forEach(securityRequirement -> {
        OpenApiCommonHelper.initSecurityRequirement(securityRequirement, securityList);
      });
      openApiPathOperationEmbedded.setSecurity(securityList);
    }
    openApiPathOperationEmbedded
        .setRequestBody(OpenApiPayloadHelper.getOpenApiRequestBody(operation.getRequestBody()));
    openApiPathOperationEmbedded.setDeprecated(operation.getDeprecated());
    openApiPathOperationEmbedded
        .setResponses(OpenApiPayloadHelper.getOpenApiComponentResponse(operation.getResponses()));
    openApiPathOperationEmbedded
        .setCallbacks(getOpenApiComponentCallBacks(operation.getCallbacks()));
    return openApiPathOperationEmbedded;
  }

  private static List<OpenApiParameterEmbedded> getPathItemParameters(List<Parameter> parameters) {
    List<OpenApiParameterEmbedded> parameterEmbeddedList = new ArrayList<>();
    parameters.forEach(parameter -> {
      OpenApiParameterEmbedded openApiParameterEmbedded = OpenApiPathHelper
          .getOpenApiParameter(parameter);
      parameterEmbeddedList.add(openApiParameterEmbedded);
    });
    return parameterEmbeddedList;
  }

  private static List<OpenApiServerEmbedded> getPathServers(List<Server> servers) {
    List<OpenApiServerEmbedded> openApiServerEmbeddedList = new ArrayList<>();
    servers.forEach(server -> {
      OpenApiServerEmbedded openApiServerEmbedded = OpenApiServerHelper.getOpenApiServers(server);
      openApiServerEmbeddedList.add(openApiServerEmbedded);
    });
    return openApiServerEmbeddedList;
  }

}
