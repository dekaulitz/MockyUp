package com.github.dekaulitz.mockyup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.entities.MockEntities;
import com.github.dekaulitz.mockyup.errorHandlers.NotFoundException;
import com.github.dekaulitz.mockyup.models.MockExample;
import com.github.dekaulitz.mockyup.models.MocksModel;
import com.github.dekaulitz.mockyup.repositories.MockRepositories;
import com.github.dekaulitz.mockyup.vmodels.MockVmodel;
import io.swagger.parser.OpenAPIParser;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
public class MockControllers {
    @Autowired
    private final MockRepositories mockRepositories;
    @Autowired
    private final MocksModel mocksModel;

    public MockControllers(MockRepositories mockRepositories, MocksModel mocksModel) {
        this.mockRepositories = mockRepositories;
        this.mocksModel = mocksModel;
    }

    /**
     * @return
     * @throws IOException
     * @desc showing swagger doc
     */
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> greeting() throws IOException {
        Resource resource = new ClassPathResource("/static/swagger.json");
        InputStreamReader isReader = new InputStreamReader(resource.getInputStream());
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }
        return ResponseEntity.ok(sb.toString());
    }

    /**
     * @param path
     * @param id
     * @param body
     * @param request
     * @return
     * @throws JsonProcessingException
     * @desc mocking the uri that registerion on mockyup
     */
    @RequestMapping(value = "/mocks/mocking/{id}", method = {RequestMethod.OPTIONS, RequestMethod.DELETE,
            RequestMethod.POST, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.PATCH, RequestMethod.PUT,
            RequestMethod.TRACE},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity mockingPath(@RequestParam(value = "path", required = false) String path,
                                      @PathVariable String id, @RequestBody(required = false) String body,
                                      HttpServletRequest request) {
        String[] originalPathUri = path.split("\\?");
        MockExample mock = null;
        try {
            mock = mocksModel.getMockMocking(request, id, originalPathUri, body);
            if (mock != null)
                return this.generateMockResponseEntity(mock);
            return new ResponseEntity<>("no example mock found", HttpStatus.NOT_FOUND);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("no example mock found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @return
     * @desc listing all mocks
     */
    @GetMapping(value = "/mocks")
    public ResponseEntity<List<MockVmodel>> mocks() {
        List<MockVmodel> mockResponseVmodels = new ArrayList<>();
        for (MockEntities mockEntities : mocksModel.getMocks()) {
            MockVmodel mockResponseVmodel = new MockVmodel();
            mockResponseVmodel.setId(mockEntities.getId());
            mockResponseVmodel.setDescription(mockEntities.getDescription());
            mockResponseVmodel.setTitle(mockEntities.getTitle());
            try {
                OpenAPI openAPI = Json.mapper().readValue(mockEntities.getSpec(), OpenAPI.class);
                Paths newPath = new Paths();
                openAPI.getPaths().forEach((s, pathItem) -> {
                    newPath.put(s.replace("_", ".").replace("*{", "{"), pathItem);
                });
                openAPI.setPaths(newPath);
                mockResponseVmodel.setSpec(openAPI);
                mockResponseVmodels.add(mockResponseVmodel);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e.getCause());
                ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok(mockResponseVmodels);
    }

    /**
     * @param id
     * @param body
     * @return
     * @desc get mocking detail
     */
    @RequestMapping(value = "/mocks/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity mockById(@PathVariable String id, @RequestBody(required = false) String body) {
        try {
            MockEntities mock = mocksModel.getMockById(id);
            MockVmodel mockResponseVmodel = new MockVmodel();
            mockResponseVmodel.setId(mock.getId());
            mockResponseVmodel.setSpec(Json.mapper().readValue(mock.getSpec(), OpenAPI.class));
            mockResponseVmodel.setDescription(mock.getDescription());
            mockResponseVmodel.setTitle(mock.getTitle());
            return ResponseEntity.ok(mockResponseVmodel);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("no example mock found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param body
     * @return
     * @throws JsonProcessingException
     * @desc storing the mocks
     */
    @PostMapping(value = "/mocks")
    public ResponseEntity storeMocksEntity(@RequestBody MockVmodel body) {
        SwaggerParseResult result = null;
        try {
            result = new OpenAPIParser().readContents(Json.mapper().writeValueAsString(body.getSpec()), null, null);
            OpenAPI openAPI = result.getOpenAPI();
            Paths newPath = new Paths();
            openAPI.getPaths().forEach((s, pathItem) -> {
                newPath.put(s.replace(".", "_").replace("{", "*{"), pathItem);
            });
            openAPI.setPaths(newPath);
            MockEntities mockEntities = new MockEntities();
            mockEntities.setTitle(body.getTitle());
            mockEntities.setDescription(body.getDescription());
            mockEntities.setSpec(Json.mapper().writeValueAsString(openAPI));
            MockEntities mock = mocksModel.storeMock(mockEntities);
            MockVmodel mockResponseVmodel = new MockVmodel();
            mockResponseVmodel.setId(mock.getId());
            mockResponseVmodel.setSpec(Json.mapper().readValue(mock.getSpec(), OpenAPI.class));
            mockResponseVmodel.setDescription(mockEntities.getDescription());
            mockResponseVmodel.setTitle(mockEntities.getTitle());
            return ResponseEntity.ok(mockResponseVmodel);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param id
     * @return
     * @desc delete mock by id
     */
    @RequestMapping(value = "/mocks/{id}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity deleteByMockId(@PathVariable String id) {
        try {
            mocksModel.deleteMock(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>("no mock found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * @param mock
     * @return
     * @desc generate mock response base mock data
     */
    private ResponseEntity<Object> generateMockResponseEntity(MockExample mock) {
        return new ResponseEntity<>(mock.getResponse().getResponse(), HttpStatus.valueOf(mock.getResponse().getHttpCode()));
    }
}
