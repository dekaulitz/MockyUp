package com.github.dekaulitz.mockyup.helperTest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.entities.MockHistoryEntities;
import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.entities.UserMocksEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.db.repositories.paging.UserEntitiesPage;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.CreatorMockVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockUserLookupVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupDetailCurrentAccessVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupDetailUpdatedByVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupDetailVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.DtoMockupHistoryVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.MockVmodel;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.UserDetailMocks;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.UserMockVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.RegistrationVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.UpdateUserVmodel;
import com.github.dekaulitz.mockyup.utils.Hash;
import com.github.dekaulitz.mockyup.utils.JsonMapper;
import com.github.dekaulitz.mockyup.utils.Role;
import com.github.dekaulitz.mockyup.utils.XmlMapper;
import io.swagger.parser.OpenAPIParser;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Helper {

  public static final String DEFAULT_MOCK_ID = "DEFAULT_MOCK_ID";
  public static final String MOCK_TITLE = "MOCK_TITLE";
  public static final String MOCK_DESC = "MOCK_DESC";
  public static final String DEFAULT_USERNAME = "root";
  public static final String DEFAULT_PASSWORD = "root";
  public static final String DEFAULT_RESPONSE_MOCK = "DEFAULT";
  public static final String DEFAULT_COMPONENT_EXAMPLE = "DEFAULT_COMPONENT_EXAMPLE";
  public static final String DEFAULT_HEADER_VALUE = "this is from mocks";
  public static final String DEFAULT_HEADER_NAME = "x-request-id";
  public static final String CAN_DO_REFRESH = "canRefresh";
  public static final String EXPIRED_AT = "expiredAt";
  public static final long ONE_MINUTE_IN_MILLIS = 600000;//millisecs
  public static final String SECRET = "something";
  private static final String DEFAULT_USER_ID = "x";
  public static OpenAPI openAPI;

  public static String generateToken(String userId, int refreshExp, int expiredAt)
      throws UnsupportedEncodingException {
    Algorithm algorithm = Algorithm.HMAC256(SECRET);
    Calendar date = Calendar.getInstance();
    long t = date.getTimeInMillis();
    return JWT.create()
        .withClaim("id", userId)
        .withClaim(CAN_DO_REFRESH,
            new Date(t + (refreshExp * ONE_MINUTE_IN_MILLIS)))
        .withClaim(EXPIRED_AT,
            new Date(t + (expiredAt + ONE_MINUTE_IN_MILLIS)))
        .withIssuedAt(new Date()).sign(algorithm);
  }

  public static String generateInvalidToken(String secret, int refreshExp, int expiredAt)
      throws UnsupportedEncodingException {
    Algorithm algorithm = Algorithm.HMAC256(secret);
    Calendar date = Calendar.getInstance();
    long t = date.getTimeInMillis();
    return JWT.create()
        .withClaim(CAN_DO_REFRESH,
            new Date(t + (refreshExp * ONE_MINUTE_IN_MILLIS)))
        .withClaim(EXPIRED_AT,
            new Date(t + (expiredAt + ONE_MINUTE_IN_MILLIS)))
        .withIssuedAt(new Date()).sign(algorithm);
  }

  public static String generateExpiredToken(String givenId) throws UnsupportedEncodingException {
    Algorithm algorithm = Algorithm.HMAC256(SECRET);
    Calendar date = Calendar.getInstance();
    long t = date.getTimeInMillis();
    return JWT.create()
        .withClaim("id", givenId)
        .withClaim(CAN_DO_REFRESH, new Date())
        .withClaim(EXPIRED_AT, new Date())
        .withIssuedAt(new Date()).sign(algorithm);
  }

  public static MockEntities getMockEntities() throws IOException {

    SwaggerParseResult result = new OpenAPIParser()
        .readContents(getDefaultJsonContract(), null, null);
    openAPI = result.getOpenAPI();
    Paths newPath = new Paths();
    openAPI.getPaths().forEach((s, pathItem) -> {
      newPath.put(s.replace("{", "*{"), pathItem);
    });
    openAPI.setPaths(newPath);
    MockEntities mockEntities = new MockEntities();
    mockEntities.setId(DEFAULT_MOCK_ID);
    mockEntities.setTitle(MOCK_TITLE);
    mockEntities.setDescription(MOCK_DESC);
    mockEntities.setSwagger(getDefaultJsonContract());
    mockEntities.setSpec(JsonMapper.mapper().writeValueAsString(getOpenApi()));
    List<UserMocksEntities> userMocksEntitiesList = new ArrayList<>();
    userMocksEntitiesList.add(UserMocksEntities.builder()
        .userId("x")
        .access(Role.MOCKS_READ_WRITE.name())
        .build());
    mockEntities.setUpdatedDate(new Date());
    mockEntities.setUsers(userMocksEntitiesList);
    return mockEntities;
  }

  public static String getDefaultJsonContract() throws IOException {
    ClassLoader classLoader = Helper.class.getClassLoader();
    //using static swagger json for testing
    File file = new File(classLoader.getResource("public/mocking_test.json").getFile());
    return new String(Files.readAllBytes(file.toPath()));
  }

  public static List<DtoMockUserLookupVmodel> getDtoMockUserLookupVmodelList() {
    List<DtoMockUserLookupVmodel> dtoMockUserLookupVmodelList = new ArrayList<>();

    List<UserDetailMocks> userDetailMocksList1 = new ArrayList<>();
    userDetailMocksList1.add(UserDetailMocks.builder()
        .id("1")
        .username("root")
        .access(Role.MOCKS_READ_WRITE.name())
        .build());
    userDetailMocksList1.add(UserDetailMocks.builder()
        .id("2")
        .username("root2")
        .access(Role.MOCKS_READ_WRITE.name())
        .build());

    List<UserDetailMocks> userDetailMocksList2 = new ArrayList<>();
    userDetailMocksList2.add(UserDetailMocks.builder()
        .id("1")
        .username("root")
        .access(Role.MOCKS_READ_WRITE.name())
        .build());
    userDetailMocksList2.add(UserDetailMocks.builder()
        .id("2")
        .username("root2")
        .access(Role.MOCKS_READ_WRITE.name())
        .build());

    dtoMockUserLookupVmodelList.add(DtoMockUserLookupVmodel.builder()
        .id("1")
        .users(userDetailMocksList1)
        .build());
    dtoMockUserLookupVmodelList.add(DtoMockUserLookupVmodel.builder()
        .id("2")
        .users(userDetailMocksList2)
        .build());
    return dtoMockUserLookupVmodelList;
  }

  public static List<DtoMockupDetailVmodel> getDtoMockupDetailVmodels() throws IOException {
    List<DtoMockupDetailVmodel> dtoMockupDetailVmodelList = new ArrayList<>();
    dtoMockupDetailVmodelList.add(DtoMockupDetailVmodel.builder()
        .title(MOCK_TITLE)
        .description(MOCK_DESC)
        .updatedBy(DtoMockupDetailUpdatedByVmodel.builder()
            .username(DEFAULT_USERNAME)
            .userId(DEFAULT_USER_ID)
            .build())
        .spec(JsonMapper.mapper().writeValueAsString(getOpenApi()))
        .currentAccessUser(DtoMockupDetailCurrentAccessVmodel.builder()
            .username(DEFAULT_USERNAME)
            .access(Role.MOCKS_READ_WRITE.name())
            .build())
        .build());
    return dtoMockupDetailVmodelList;
  }

  public static MockHistoryEntities getMockHistoryEntities() throws IOException {
    return MockHistoryEntities.builder()
        .id(DEFAULT_MOCK_ID)
        .title(MOCK_TITLE)
        .description(MOCK_DESC)
        .spec(JsonMapper.mapper().writeValueAsString(getOpenApi()))
        .swagger(getDefaultJsonContract())
        .build();
  }

  public static List<MockHistoryEntities> getMockHistoryEntitiesList() throws IOException {
    List<MockHistoryEntities> mockHistoryEntitiesList = new ArrayList<>();
    mockHistoryEntitiesList.add(getMockHistoryEntities());
    return mockHistoryEntitiesList;
  }

  public static DtoMockupHistoryVmodel getDtoMockupHistoryVmodel() throws IOException {
    return DtoMockupHistoryVmodel.builder()
        .mockId(DEFAULT_MOCK_ID)
        .swagger(getDefaultJsonContract())
        .build();
  }

  public static List<MockEntities> getMockEntitiesList() throws IOException {
    List<MockEntities> mockEntitiesList = new ArrayList<>();
    mockEntitiesList.add(getMockEntities());
    return mockEntitiesList;
  }

  public static OpenAPI getOpenApi() throws IOException {
    SwaggerParseResult result = new OpenAPIParser()
        .readContents(getDefaultJsonContract(), null, null);
    openAPI = result.getOpenAPI();
    Paths newPath = new Paths();
    openAPI.getPaths().forEach((s, pathItem) -> {
      newPath.put(s.replace("{", "*{"), pathItem);
    });
    openAPI.setPaths(newPath);
    return openAPI;
  }

  public static MockVmodel getMockVmodel() throws IOException {
    List<UserMockVmodel> userMockVmodels = new ArrayList<>();
    userMockVmodels.add(UserMockVmodel.builder()
        .access(Role.MOCKS_READ_WRITE.name())
        .userId(DEFAULT_USER_ID)
        .build());
    return MockVmodel.builder()
        .title(MOCK_TITLE)
        .description(MOCK_DESC)
        .spec(Json.mapper().readTree(getDefaultJsonContract()))
        .users(userMockVmodels)
        .updatedBy(CreatorMockVmodel.builder()
            .userId(DEFAULT_USER_ID)
            .username(DEFAULT_USERNAME)
            .build())
        .build();
  }

  public static MockEntitiesPage getMockEntitiesPage() throws IOException {
    MockEntitiesPage mockEntitiesPage = new MockEntitiesPage();
    mockEntitiesPage.setPage(1);
    mockEntitiesPage.setPageCount(10);
    mockEntitiesPage.setSize(10);
    mockEntitiesPage.setRows(getMockEntitiesList());
    return mockEntitiesPage;
  }


  public static RegistrationVmodel getreRegistrationVmodel() {
    List<String> accessList = new ArrayList<>();
    accessList.add(Role.MOCKS_READ_WRITE.name());
    accessList.add(Role.USERS_READ_WRITE.name());
    return RegistrationVmodel.builder()
        .accessList(accessList)
        .password(DEFAULT_PASSWORD)
        .username(UUID.randomUUID().toString())
        .build();
  }

  public static UserEntities getUserEntities() {
    List<String> accessList = new ArrayList<>();
    accessList.add(Role.USERS_READ.name());
    accessList.add(Role.MOCKS_READ.name());
    return UserEntities.builder()
        .accessList(accessList)
        .password(Hash.hashing(DEFAULT_PASSWORD))
        .username("default")
        .build();
  }

  public static List<UserEntities> getUserEntitiesList() {
    List<UserEntities> userEntitiesList = new ArrayList<>();
    userEntitiesList.add(getUserEntities());
    return userEntitiesList;
  }

  public static UserEntitiesPage getUserEntitiesPage() {
    UserEntitiesPage userEntitiesPage = new UserEntitiesPage();
    userEntitiesPage.setPage(1);
    userEntitiesPage.setPageCount(10);
    userEntitiesPage.setSize(10);
    userEntitiesPage.setRows(getUserEntitiesList());
    return userEntitiesPage;
  }

  public static UpdateUserVmodel getUpdateUserVmodel() {
    List<String> accessList = new ArrayList<>();
    accessList.add(Role.MOCKS_READ_WRITE.name());
    accessList.add(Role.USERS_READ.name());
    return UpdateUserVmodel.builder()
        .accessList(accessList)
        .password(DEFAULT_PASSWORD)
        .username(DEFAULT_USERNAME + 1)
        .build();
  }

  public static AddUserAccessVmodel getAddUserAccessVmodel() {
    return AddUserAccessVmodel.builder()
        .userId(DEFAULT_USER_ID)
        .access(Role.MOCKS_READ_WRITE.name())
        .build();
  }

  public static String parseXmlComponentTest(String xml) throws IOException {
    JsonNode jsonNode = XmlMapper.mapper().readTree(xml.getBytes());
    return jsonNode.get("name").asText();
  }

}
