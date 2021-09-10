package com.github.dekaulitz.mockyup.server.service.mockup.helper.mockup;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dekaulitz.mockyup.server.db.entities.embeddable.openapi.embedded.OpenApiPathOperationEmbedded;
import com.github.dekaulitz.mockyup.server.db.entities.features.constants.DevStockEnum;
import com.github.dekaulitz.mockyup.server.db.entities.features.mockup.MockUpRequestEmbedded;
import com.github.dekaulitz.mockyup.server.utils.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

@Slf4j
public class DevStockHelper {

  public static void setupOperationPathConfiguration(
      OpenApiPathOperationEmbedded openApiPathOperation) {
    System.out.println(openApiPathOperation);
    if (openApiPathOperation == null || MapUtils.isEmpty(openApiPathOperation.getExtensions())) {
      return;
    }
    openApiPathOperation.getExtensions().forEach((s, extension) -> {
      DevStockEnum devStockEnum = DevStockEnum.get(s);
      if (devStockEnum == null) {
        return;
      }
      initMockConfiguration(devStockEnum, openApiPathOperation, extension);
    });
  }

  private static void initMockConfiguration(DevStockEnum devStockEnum,
      OpenApiPathOperationEmbedded openApiPathEmbedded, Object extension) {
    if (devStockEnum != DevStockEnum.MOCKING_REQUEST) {
      return;
    }
    JsonNode mockupNode = JsonMapper.mapper().convertValue(extension, JsonNode.class);
    if (mockupNode == null) {
      log.debug("initConfiguration invalid x-mock-request {}", extension);
      throw new RuntimeException("invalid x-mock-request  " + extension.toString());
    }
    MockUpRequestEmbedded mockUpRequestEmbedded = new MockUpRequestEmbedded();
    mockUpRequestEmbedded.setMockingRequestBodies(
        MockupHelper.getMatchingAttributes(mockupNode, "mockingRequestBodies"));
    mockUpRequestEmbedded.setMockingRequestHeaders(
        MockupHelper.getMatchingAttributes(mockupNode, "mockingRequestHeaders"));
    mockUpRequestEmbedded.setMockingRequestPaths(
        MockupHelper.getMatchingAttributes(mockupNode, "mockingRequestPaths"));
    mockUpRequestEmbedded.setMockingRequestQueries(
        MockupHelper.getMatchingAttributes(mockupNode, "mockingRequestQueries"));
    mockUpRequestEmbedded.setMockingDefaultResponse(MockupHelper.getResponseDefault(mockupNode));
    openApiPathEmbedded.setMockup(mockUpRequestEmbedded);
  }

}
