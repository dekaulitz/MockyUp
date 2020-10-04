package com.github.dekaulitz.mockyup.utils;

import com.github.dekaulitz.mockyup.helperTest.Helper;
import io.swagger.v3.oas.models.media.Schema;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

class OpenAPIToolsTest {

  @Test
  void getSimpleRef() {
    String refName = OpenAPITools.getSimpleRef("#/components/examples/DEFAULT_COMPONENT_EXAMPLE");
    Assert.isTrue(refName.equals("DEFAULT_COMPONENT_EXAMPLE"), "reff is not expected");
  }

  @Test
  void getSchemaFromName() throws IOException {
    String refName = OpenAPITools.getSimpleRef("#/components/schemas/RESPONSE");
    Schema schema = OpenAPITools.getSchemaFromName(refName, Helper.getOpenApi());
    Assert.isTrue(schema != null, "schema is not expected");
  }

  @Test
  void getSchemaFromRefSchema() throws IOException {
    String refName = OpenAPITools.getSimpleRef("#/components/schemas/RESPONSE_GENERAL");
    Schema schema = OpenAPITools.getSchemaFromName(refName, Helper.getOpenApi());
    Assert.isTrue(schema != null, "schema is not expected");
    Schema schema2 = OpenAPITools.getSchemaFromRefSchema(schema, Helper.getOpenApi());
    Assert.isTrue(schema2 != null, "schema is not expected");
  }
}
