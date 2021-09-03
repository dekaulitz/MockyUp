package com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.mockup;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.schemas.BaseSchema;
import com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi.OpenApiSchemaHelper;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.MapUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MockingMatchingAttributeEmbedded implements Serializable {

  private String description;
  private Map<String, BaseSchema> properties;
}
