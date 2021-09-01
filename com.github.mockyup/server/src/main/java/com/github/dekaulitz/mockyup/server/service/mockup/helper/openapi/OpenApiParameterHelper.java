package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiParameterPosition;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiParameterStyle;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiParameterEmbedded;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.EnumUtils;

public class OpenApiParameterHelper {

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
        .setExamples(OpenApiCommonHelper.initOpenApiComponentExamples(parameter.getExamples()));
    openApiParameterEmbedded.setSchema(OpenApiSchemaHelper.convertSchema(parameter.getSchema()));
    openApiParameterEmbedded.setExample(parameter.getExample());
    openApiParameterEmbedded
        .setContent(OpenApiPayloadHelper.initOpenApiComponentContent(parameter.getContent()));
    return openApiParameterEmbedded;
  }
}
