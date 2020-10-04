package com.github.dekaulitz.mockyup.base.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.util.Assert.isTrue;

import com.github.dekaulitz.mockyup.controllers.BaseTest;
import com.github.dekaulitz.mockyup.db.repositories.MockRepository;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.MockVmodel;
import com.github.dekaulitz.mockyup.helperTest.Helper;
import com.github.dekaulitz.mockyup.infrastructure.errors.vmodels.ResponseVmodel;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdviceControllerTest extends BaseTest {

  @MockBean
  protected MockRepository mockRepository;

  @Test
  void handleMethodArgumentNotValid() throws IOException {
    //given /mocks/store
    MockVmodel mockVmodel = new MockVmodel();
    when(this.mockRepository.save(any())).thenReturn(Helper.getMockEntities());

    //when user do request
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + Helper.generateToken(givenId, 10, 10));
    HttpEntity<MockVmodel> request1 = new HttpEntity<>(mockVmodel, headers);
    ResponseEntity<ResponseVmodel> response1 = this.restTemplate
        .exchange(baseUrl + "/mocks/store", HttpMethod.POST, request1, ResponseVmodel.class);
    isTrue(response1.getStatusCode().value() == ResponseCode.VALIDATION_FAIL.getHttpCode().value(),
        HTTPCODE_NOT_EXPECTED);
    isTrue(
        response1.getBody().getResponseCode().equals(ResponseCode.VALIDATION_FAIL.getErrorCode()),
        RESPONSE_BODY_NOT_EXPECTED);
  }
}
