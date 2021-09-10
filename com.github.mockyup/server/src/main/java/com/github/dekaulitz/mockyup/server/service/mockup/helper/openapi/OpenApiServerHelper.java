package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.OpenApiServerEmbedded;
import io.swagger.v3.oas.models.servers.Server;

public class OpenApiServerHelper {

  protected static OpenApiServerEmbedded getOpenApiServers(Server server) {
    if (server == null) {
      return null;
    }
    // cannot use model mapper issue on variables when mapping
    OpenApiServerEmbedded openApiServerEmbedded = new OpenApiServerEmbedded();
    openApiServerEmbedded.setUrl(server.getUrl());
    openApiServerEmbedded.setDescription(server.getDescription());
    openApiServerEmbedded.setVariables(server.getVariables());
    openApiServerEmbedded.setExtensions(server.getExtensions());
    return openApiServerEmbedded;
  }
}
