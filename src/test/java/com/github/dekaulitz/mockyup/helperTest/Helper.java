package com.github.dekaulitz.mockyup.helperTest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.dekaulitz.mockyup.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.db.entities.MockHistoryEntities;
import com.github.dekaulitz.mockyup.db.entities.UserEntities;
import com.github.dekaulitz.mockyup.db.entities.UserMocksEntities;
import com.github.dekaulitz.mockyup.db.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.db.repositories.paging.UserEntitiesPage;
import com.github.dekaulitz.mockyup.domain.mocks.vmodels.*;
import com.github.dekaulitz.mockyup.domain.users.vmodels.AddUserAccessVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.RegistrationVmodel;
import com.github.dekaulitz.mockyup.domain.users.vmodels.UpdateUserVmodel;
import com.github.dekaulitz.mockyup.utils.Hash;
import com.github.dekaulitz.mockyup.utils.JsonMapper;
import com.github.dekaulitz.mockyup.utils.JwtManager;
import com.github.dekaulitz.mockyup.utils.Role;
import io.swagger.parser.OpenAPIParser;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.core.models.SwaggerParseResult;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.*;

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
    private static final String DEFAULT_USER_ID = "x";
    public static OpenAPI openAPI;

    public static String generateToken(String userId, int refreshExp, int expiredAt) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(JwtManager.SECCRET);
        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        return JWT.create()
                .withClaim("id", userId)
                .withClaim(JwtManager.CAN_DO_REFRESH, new Date(t + (refreshExp * JwtManager.ONE_MINUTE_IN_MILLIS)))
                .withClaim(JwtManager.EXPIRED_AT, new Date(t + (expiredAt + JwtManager.ONE_MINUTE_IN_MILLIS)))
                .withIssuedAt(new Date()).sign(algorithm);
    }

    public static String generateInvalidToken(String secret, int refreshExp, int expiredAt) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        return JWT.create()
                .withClaim(JwtManager.CAN_DO_REFRESH, new Date(t + (refreshExp * JwtManager.ONE_MINUTE_IN_MILLIS)))
                .withClaim(JwtManager.EXPIRED_AT, new Date(t + (expiredAt + JwtManager.ONE_MINUTE_IN_MILLIS)))
                .withIssuedAt(new Date()).sign(algorithm);
    }

    public static String generateExpiredToken(String givenId) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(JwtManager.SECCRET);
        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        return JWT.create()
                .withClaim("id", givenId)
                .withClaim(JwtManager.CAN_DO_REFRESH, new Date())
                .withClaim(JwtManager.EXPIRED_AT, new Date())
                .withIssuedAt(new Date()).sign(algorithm);
    }

    public static MockEntities getMockEntities() throws IOException {

        SwaggerParseResult result = new OpenAPIParser().readContents(getDefaultJsonContract(), null, null);
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
        mockEntities.setSpec(JsonMapper.mapper().writeValueAsString(openAPI));
        List<UserMocksEntities> userMocksEntitiesList = new ArrayList<>();
        userMocksEntitiesList.add(UserMocksEntities.builder()
                .userId("x")
                .access(Role.MOCKS_READ_WRITE.name())
                .build());
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
                .spec(getDefaultJsonContract())
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
                .spec(getDefaultJsonContract())
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

    public static OpenAPI generateOpenAoi() throws IOException {
        SwaggerParseResult result = new OpenAPIParser().readContents(getDefaultJsonContract(), null, null);
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
}
