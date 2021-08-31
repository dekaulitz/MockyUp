package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.embedded.OpenApiXmlEmbedded;
import io.swagger.v3.oas.models.media.XML;

public class OpenApiXmlHelper {

  public static OpenApiXmlEmbedded initOpenApiXml(XML xml) {
    if (xml == null) {
      return null;
    }
    OpenApiXmlEmbedded openApiXmlEmbedded = new OpenApiXmlEmbedded();
    openApiXmlEmbedded.setName(xml.getName());
    openApiXmlEmbedded.setNamespace(xml.getNamespace());
    openApiXmlEmbedded.setPrefix(xml.getPrefix());
    openApiXmlEmbedded.setAttribute(xml.getAttribute());
    openApiXmlEmbedded.setWrapped(xml.getWrapped());
    openApiXmlEmbedded.setExtensions(xml.getExtensions());
    return openApiXmlEmbedded;
  }
}
