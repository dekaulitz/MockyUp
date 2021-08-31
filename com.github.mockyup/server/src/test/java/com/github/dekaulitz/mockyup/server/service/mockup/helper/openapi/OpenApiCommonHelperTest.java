package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OpenApiCommonHelperTest {

  @Test
  void getContentType() {
    String contentType = "Application/json";
    OpenApiContentType contentTypeResult = OpenApiCommonHelper.getContentType(contentType);
    Assertions.assertThat(contentTypeResult)
        .isEqualByComparingTo(OpenApiContentType.APPLICATION_JSON);
    contentType = "Application/xml";
    contentTypeResult = OpenApiCommonHelper.getContentType(contentType);
    Assertions.assertThat(contentTypeResult)
        .isEqualByComparingTo(OpenApiContentType.APPLICATION_XML);
  }
}