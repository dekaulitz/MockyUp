package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiPathEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiServerEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiPathHttpMethod;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiCallbackEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiParameterEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiPathOperationEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiSecurityEmbedded;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

public class OpenApiPathHelper {

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

  protected static void getOpenApiPathInformation(String path, Map<String, Object> extensions,
      PathItem pathItem, List<OpenApiPathEmbedded> openApiPathEmbeddedList) {
    getCommonHttpRequestOpenApi(path, extensions, pathItem, openApiPathEmbeddedList);
    getRareHttpRequestOpenApi(path, extensions, pathItem, openApiPathEmbeddedList);
  }


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

  /**
   * @TODO need passing component {@link io.swagger.v3.oas.models.Components} object for injecting
   * schemas or model
   */
  private static OpenApiPathEmbedded getOpenApiHttpRequest(String path, PathItem pathItem,
      Operation operation, OpenApiPathHttpMethod openApiPathHttpMethod,
      Map<String, Object> extensions) {
    OpenApiPathEmbedded openApiPathEmbedded = new OpenApiPathEmbedded();
    openApiPathEmbedded.setPath(path);
    openApiPathEmbedded.setHttpMethod(openApiPathHttpMethod);
    openApiPathEmbedded.setExtensions(extensions);
    openApiPathEmbedded.set$ref(pathItem.get$ref());
    OpenApiPathOperationEmbedded openApiPathOperationEmbedded = getOpenApiPathOperation(pathItem,
        operation);
    openApiPathEmbedded.setOperation(openApiPathOperationEmbedded);
    return openApiPathEmbedded;
  }

  private static OpenApiPathOperationEmbedded getOpenApiPathOperation(
      PathItem pathItem, Operation operation) {
    OpenApiPathOperationEmbedded openApiPathOperationEmbedded = new OpenApiPathOperationEmbedded();
    openApiPathOperationEmbedded.setSummary(pathItem.getSummary());
    openApiPathOperationEmbedded.setDescription(pathItem.getDescription());
    if (CollectionUtils.isNotEmpty(pathItem.getServers())) {
      List<OpenApiServerEmbedded> openApiServerEmbeddedList = new ArrayList<>();
      pathItem.getServers().forEach(server -> {
        OpenApiServerEmbedded openApiServerEmbedded = OpenApiServerHelper.getOpenApiServers(server);
        openApiServerEmbeddedList.add(openApiServerEmbedded);
      });
      openApiPathOperationEmbedded.setServers(openApiServerEmbeddedList);
    }
    if (CollectionUtils.isNotEmpty(pathItem.getParameters())) {
      List<OpenApiParameterEmbedded> parameterEmbeddedList = new ArrayList<>();
      pathItem.getParameters().forEach(parameter -> {
        OpenApiParameterEmbedded openApiParameterEmbedded = OpenApiParameterHelper
            .getOpenApiParameter(parameter);
        parameterEmbeddedList.add(openApiParameterEmbedded);
      });
      openApiPathOperationEmbedded.setParameters(parameterEmbeddedList);
    }
    openApiPathOperationEmbedded.setTags(operation.getTags());
    openApiPathOperationEmbedded.setExternalDocs(operation.getExternalDocs());
    openApiPathOperationEmbedded.setExtensions(operation.getExtensions());
    openApiPathOperationEmbedded.setOperationId(operation.getOperationId());
    if (CollectionUtils.isNotEmpty(operation.getSecurity())) {
      List<OpenApiSecurityEmbedded> securityList = new ArrayList<>();
      operation.getSecurity().forEach(securityRequirement -> {
        initSecurityRequirement(securityRequirement, securityList);
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

  private static void initSecurityRequirement(SecurityRequirement securityRequirement,
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
