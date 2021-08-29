package com.github.dekaulitz.mockyup.server.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.github.dekaulitz.mockyup.server.configuration.jwt.JwtManager;
import com.github.dekaulitz.mockyup.server.db.entities.v1.UserEntities;
import com.github.dekaulitz.mockyup.server.db.repositories.UserRepository;
import com.github.dekaulitz.mockyup.server.model.constants.Role;
import com.github.dekaulitz.mockyup.server.service.mockup.helper.HashingHelper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc()
@ActiveProfiles("test")
public abstract class BaseTest {

  protected final String MEDIA_TYPE_ACCEPT = "accept";
  protected final String HTTPCODE_NOT_EXPECTED = "http code is not expected";
  protected final String RESPONSE_BODY_NOT_EXPECTED = "response body is not expected";
  protected String givenId = "x";
  protected String givenUserName = "root";
  protected String givenPassword = "root";

  @MockBean
  protected UserRepository userRepository;
  @LocalServerPort
  protected int port;
  protected String baseUrl;
  @Autowired
  protected TestRestTemplate restTemplate;

  @Autowired
  protected JwtManager jwtManager;

  @Autowired
  protected MockMvc mockMvc;

  @BeforeEach
  void setup() {
    baseUrl = "http://localhost:" + port;
    List<String> givenAccesses = new ArrayList<>();
    givenAccesses.add(Role.USERS_READ_WRITE.name());
    givenAccesses.add(Role.MOCKS_READ_WRITE.name());
    when(this.userRepository.findById(any()))
        .thenReturn(java.util.Optional.ofNullable(UserEntities.builder()
            .id(givenId)
            .username(givenUserName)
            .password(HashingHelper.hashing(givenPassword))
            .accessList(givenAccesses)
            .build()));
  }
}
