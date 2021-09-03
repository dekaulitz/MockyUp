package com.github.dekaulitz.mockyup.server.service.mockup.helper.openapi;

import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiContentType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.embeddable.openapi.constants.OpenApiSecurityInType;
import com.github.dekaulitz.mockyup.server.db.entities.v2.features.constants.DevStockEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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

  @Test
  void checkEnum() {
    Assertions.assertThat(DevStockEnum.get("x-mock-example"))
        .isEqualTo(DevStockEnum.MOCKING_REQUEST);
  }
}
