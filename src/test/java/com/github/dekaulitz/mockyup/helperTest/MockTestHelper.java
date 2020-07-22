package com.github.dekaulitz.mockyup.helperTest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.dekaulitz.mockyup.domain.auth.vmodels.DoAuthVmodel;
import com.github.dekaulitz.mockyup.domain.auth.vmodels.DtoAuthProfileVmodel;
import com.github.dekaulitz.mockyup.infrastructure.db.entities.MockEntities;
import com.github.dekaulitz.mockyup.utils.JsonMapper;
import com.github.dekaulitz.mockyup.utils.JwtManager;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import lombok.Getter;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;

public class MockTestHelper {
    public final String DEFAULT_MOCK_ID = "DEFAULT_MOCK_ID";
    public final String MOCK_TITLE = "MOCK_TITLE";
    public final String MOCK_DESC = "MOCK_DESC";

    public final String DEFAULT_RESPONSE_MOCK = "DEFAULT";
    public final String DEFAULT_COMPONENT_EXAMPLE = "DEFAULT_COMPONENT_EXAMPLE";
    public final String DEFAULT_HEADER_VALUE = "this is from mocks";
    public final String DEFAULT_HEADER_NAME = "x-request-id";

    @Getter
    private TestRestTemplate restTemplate;
    @Getter
    private String baseUrl;
    @Getter
    private String defaultJsonContract;
    @Getter
    private OpenAPI openAPI;

    public MockTestHelper(TestRestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public MockTestHelper() {
    }

    public String doRequestLogin() {
        DoAuthVmodel authVmodel = new DoAuthVmodel();
        authVmodel.setUsername("root");
        authVmodel.setPassword("root");
        HttpEntity<DoAuthVmodel> request = new HttpEntity<>(authVmodel);
        ResponseEntity<DtoAuthProfileVmodel> DtoAuthProfileVmodel = this.restTemplate
                .postForEntity(baseUrl + "/mocks/login", request, DtoAuthProfileVmodel.class);
        Assert.isTrue(DtoAuthProfileVmodel.getStatusCode().value() == 200, "http code unexpected");
        Assert.isTrue(!DtoAuthProfileVmodel.getBody().getToken().isEmpty(), "token not exist");
        return DtoAuthProfileVmodel.getBody().getToken();
    }

    public MockEntities generateMockEntities() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        //using static swagger json for testing
        File file = new File(classLoader.getResource("public/mocking_test.json").getFile());
        this.defaultJsonContract = new String(Files.readAllBytes(file.toPath()));
        SwaggerParseResult result = new OpenAPIParser().readContents(this.defaultJsonContract, null, null);
        this.openAPI = result.getOpenAPI();
        Paths newPath = new Paths();
        this.openAPI.getPaths().forEach((s, pathItem) -> {
            newPath.put(s.replace("{", "*{"), pathItem);
        });
        this.openAPI.setPaths(newPath);
        MockEntities mockEntities = new MockEntities();
        mockEntities.setId(DEFAULT_MOCK_ID);
        mockEntities.setTitle(MOCK_TITLE);
        mockEntities.setDescription(MOCK_DESC);
        mockEntities.setSwagger(this.defaultJsonContract);
        mockEntities.setSpec(JsonMapper.mapper().writeValueAsString(this.openAPI));
        return mockEntities;
    }


    public String generateTokenExpired() throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(JwtManager.SECCRET);
        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();
        return JWT.create()
                .withClaim("id", "fake")
                .withClaim(JwtManager.CAN_DO_REFRESH, new Date(t))
                .withClaim(JwtManager.EXPIRED_AT, new Date(t))
                .withIssuedAt(new Date()).sign(algorithm);
    }
}
