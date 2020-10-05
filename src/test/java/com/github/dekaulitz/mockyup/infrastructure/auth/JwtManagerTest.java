package com.github.dekaulitz.mockyup.infrastructure.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.dekaulitz.mockyup.infrastructure.errors.handlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.infrastructure.security.AuthenticationProfileModel;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JwtManagerTest {

  private final static String SECRET = "SOMETHING";
  private final static Long REFRESH = 15000L;
  private final static Long EXPIRED = 15000L;
  @InjectMocks
  private JwtManager jwtManager;
  @Mock
  private JwtProperties jwtProperties;

  @Test
  public void generateToken() throws UnsupportedEncodingException {
    Mockito.when(jwtProperties.getSecret()).thenReturn(SECRET);
    Mockito.when(jwtProperties.getExpiredTime()).thenReturn(EXPIRED);
    Mockito.when(jwtProperties.getRefreshTime()).thenReturn(REFRESH);
    AuthenticationProfileModel auth = jwtManager
        .generateToken("x");
    assertThat(auth.getId()).isEqualTo("x");

    AuthenticationProfileModel authB = jwtManager
        .validateToken(auth.getAccessToken());
    assertThat(authB.getId()).isEqualTo("x");

    Optional<String> userId = jwtManager
        .getUserIdFromToken(auth.getAccessToken());
    assertThat(userId.get()).isEqualTo("x");
  }

  @Test
  public void getAuthorizationHeader() throws UnsupportedEncodingException {
    Mockito.when(jwtProperties.getSecret()).thenReturn(SECRET);
    Mockito.when(jwtProperties.getExpiredTime()).thenReturn(EXPIRED);
    Mockito.when(jwtProperties.getRefreshTime()).thenReturn(REFRESH);
    AuthenticationProfileModel auth = jwtManager
        .generateToken("x");
    assertThat(auth.getId()).isEqualTo("x");

    String token = jwtManager
        .getAuthorizationHeader("Bearer " + auth.getAccessToken());
    assertThat(auth.getAccessToken()).isEqualTo(token);
  }

  @Test
  public void getAuthorizationHeader_whenRequestWithInvalidSecret()
      throws UnsupportedEncodingException {
    Mockito.when(jwtProperties.getSecret()).thenReturn(SECRET).thenReturn("invalid_secret")
        .thenReturn(SECRET);
    Mockito.when(jwtProperties.getExpiredTime()).thenReturn(EXPIRED);
    Mockito.when(jwtProperties.getRefreshTime()).thenReturn(REFRESH);
    AuthenticationProfileModel auth = jwtManager
        .generateToken("x");
    assertThat(auth.getId()).isEqualTo("x");

    String token = jwtManager
        .getAuthorizationHeader("Bearer " + auth.getAccessToken());
    assertThat(auth.getAccessToken()).isEqualTo(token);
    try {
      jwtManager
          .validateToken(auth.getAccessToken());
    } catch (Exception e) {
      assertThat(e).isInstanceOf(UnathorizedAccess.class);
    }
  }

  @Test
  public void getAuthorizationHeader_whenRequestWithInvalidTokenExpired()
      throws UnsupportedEncodingException {
    Mockito.when(jwtProperties.getSecret()).thenReturn(SECRET)
        .thenReturn(SECRET);
    Mockito.when(jwtProperties.getExpiredTime()).thenReturn(0L);
    Mockito.when(jwtProperties.getRefreshTime()).thenReturn(0L);
    AuthenticationProfileModel auth = jwtManager
        .generateToken("x");
    assertThat(auth.getId()).isEqualTo("x");

    String token = jwtManager
        .getAuthorizationHeader("Bearer " + auth.getAccessToken());
    assertThat(auth.getAccessToken()).isEqualTo(token);
    try {
      jwtManager
          .validateToken(auth.getAccessToken());
    } catch (Exception e) {
      assertThat(e).isInstanceOf(UnathorizedAccess.class);
    }
  }

  @Test
  public void getAuthorizationHeader_whenRequestWithInvalidTokenRefresh()
      throws UnsupportedEncodingException {
    Mockito.when(jwtProperties.getSecret()).thenReturn(SECRET)
        .thenReturn(SECRET);
    Mockito.when(jwtProperties.getExpiredTime()).thenReturn(0L);
    Mockito.when(jwtProperties.getRefreshTime()).thenReturn(1000L);
    AuthenticationProfileModel auth = jwtManager
        .generateToken("x");
    assertThat(auth.getId()).isEqualTo("x");

    String token = jwtManager
        .getAuthorizationHeader("Bearer " + auth.getAccessToken());
    assertThat(auth.getAccessToken()).isEqualTo(token);
    try {
      jwtManager
          .validateToken(auth.getAccessToken());
    } catch (Exception e) {
      assertThat(e).isInstanceOf(UnathorizedAccess.class);
    }
  }

  @Test
  public void getAuthorizationHeader_whenGetTokenWithoutBearer()
      throws UnsupportedEncodingException {
    Mockito.when(jwtProperties.getSecret()).thenReturn(SECRET)
        .thenReturn(SECRET);
    Mockito.when(jwtProperties.getExpiredTime()).thenReturn(0L);
    Mockito.when(jwtProperties.getRefreshTime()).thenReturn(1000L);
    AuthenticationProfileModel auth = jwtManager
        .generateToken("x");
    assertThat(auth.getId()).isEqualTo("x");
    try {
      jwtManager
          .getAuthorizationHeader(" " + auth.getAccessToken());
    } catch (Exception e) {
      assertThat(e).isInstanceOf(UnathorizedAccess.class);
    }
  }

  @Test
  public void initJwtProperties() {
    JwtProperties properties = new JwtProperties();
    properties.setExpiredTime(1L);
    properties.setRefreshTime(2L);
    properties.setSecret("something");
    assertThat(properties.getSecret()).isEqualTo("something");
    assertThat(properties.getExpiredTime()).isEqualTo(1L);
    assertThat(properties.getRefreshTime()).isEqualTo(2L);
  }
}
