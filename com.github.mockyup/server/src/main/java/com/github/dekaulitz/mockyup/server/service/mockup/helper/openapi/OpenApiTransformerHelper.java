package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiComponents;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiPathEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiProjectInfoEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiServerEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.OpenApiTagEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiSecurityInType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiSecurityType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiOauthFlowEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiOauthFlowsEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiParameterEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiRequestBodyEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiSecuritySchemaEmbedded;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.ArrayList;
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

  public static OpenApiProjectInfoEmbedded initOpenApiInfo(Info info, ModelMapper modelMapper) {
    return modelMapper
        .map(info, OpenApiProjectInfoEmbedded.class);
  }

  public static List<OpenApiServerEmbedded> getOpenApiServers(List<Server> servers) {
    if (CollectionUtils.isEmpty(servers)) {
      return null;
    }
    List<OpenApiServerEmbedded> openApiServerEmbeddedList = new ArrayList<>();
    servers.forEach(server -> {
      OpenApiServerEmbedded openApiServerEmbedded = OpenApiServerHelper.getOpenApiServers(server);
      openApiServerEmbeddedList.add(openApiServerEmbedded);
    });
    return openApiServerEmbeddedList;
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
      OpenApiPathHelper.getOpenApiPathInformation(path, paths.getExtensions(), pathItem,
          openApiPathEmbeddedList);
    });
    return openApiPathEmbeddedList;
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
    openApiComponents
        .setLinks(OpenApiPayloadHelper.getOpenApiComponentLinks(components.getLinks()));
    openApiComponents
        .setResponses(OpenApiPayloadHelper.getOpenApiComponentResponse(components.getResponses()));
    openApiComponents
        .setRequestBodies(initOpenApiComponentRequesBody(components.getRequestBodies()));
    openApiComponents
        .setSecuritySchemes(initOpenApiComponentSecuritySchemas(components.getSecuritySchemes()));
    openApiComponents
        .setSchemas(OpenApiSchemaHelper.initOpenApiComponentSchema(components.getSchemas()));
    openApiComponents
        .setCallbacks(OpenApiPathHelper.getOpenApiComponentCallBacks(components.getCallbacks()));
    return openApiComponents;
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
      OpenApiRequestBodyEmbedded openApiRequestBodyEmbedded = OpenApiPayloadHelper
          .getOpenApiRequestBody(requestBody);
      openApiRequestBodyEmbeddedMap.put(s, openApiRequestBodyEmbedded);
    });
    return openApiRequestBodyEmbeddedMap;
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
      OpenApiParameterEmbedded openApiParameterEmbedded = OpenApiParameterHelper
          .getOpenApiParameter(parameter);
      openApiParameterEmbeddedMap.put(s, openApiParameterEmbedded);
    });
    return openApiParameterEmbeddedMap;
  }


}
