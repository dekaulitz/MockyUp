package com.github.dekaulitz.mockyup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.dekaulitz.mockyup.configuration.logs.LogsMapper;
import com.github.dekaulitz.mockyup.configuration.security.AuthenticationProfileModel;
import com.github.dekaulitz.mockyup.entities.MockEntities;
import com.github.dekaulitz.mockyup.entities.UserMocksEntities;
import com.github.dekaulitz.mockyup.errorhandlers.InvalidMockException;
import com.github.dekaulitz.mockyup.errorhandlers.NotFoundException;
import com.github.dekaulitz.mockyup.errorhandlers.UnathorizedAccess;
import com.github.dekaulitz.mockyup.models.MockModel;
import com.github.dekaulitz.mockyup.models.helper.MockExample;
import com.github.dekaulitz.mockyup.repositories.paging.MockEntitiesPage;
import com.github.dekaulitz.mockyup.utils.ResponseCode;
import com.github.dekaulitz.mockyup.vmodels.MockVmodel;
import com.github.dekaulitz.mockyup.vmodels.UserMocks;
import io.swagger.util.Json;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import lombok.NonNull;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
//@Log4j2
public class MockControllers extends BaseController {
    @Autowired
    private final MongoTemplate mongoTemplate;

    @Autowired
    private final MockModel mockModel;

    public MockControllers(LogsMapper logsMapper, MongoTemplate mongoTemplate, MockModel mockModel) {
        super(logsMapper);
        this.mongoTemplate = mongoTemplate;
        this.mockModel = mockModel;
    }

    /**
     * @return
     * @throws IOException
     * @desc showing swagger doc
     */
    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> greeting() throws IOException {
        Resource resource = new ClassPathResource("/public/swagger.json");
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
    public ResponseEntity mockingPath(@NonNull @RequestParam(value = "path") String path,
                                      @PathVariable String id, @RequestBody(required = false) String body,
                                      HttpServletRequest request, HttpServletResponse response) {
        MockExample mock = null;
        try {
            mock = this.mockModel.getMockMocking(request, path, id, body);
            if (mock != null) {
                return this.generateMockResponseEntity(mock);
            }
            return new ResponseEntity<>("no example mock found", HttpStatus.NOT_FOUND);
        } catch (NotFoundException e) {
            return this.handlingErrorResponse(e.getErrorModel(), e);
        } catch (InvalidMockException invalidMock) {
            return this.handlingErrorResponse(invalidMock.getErrorModel(), invalidMock);
        } catch (Exception e) {
            return this.handlingErrorResponse(null, e);
        }
    }

    /**
     * @return
     * @desc listing all mocks
     */
    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/list")
    public ResponseEntity mocks() {
        return ResponseEntity.ok(this.mockModel.all());
    }

    /**
     * @param id
     * @param body
     * @return
     * @desc get mocking detail
     */
    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity mockById(@PathVariable String id, @RequestBody(required = false) String body, @RequestParam(value = "spec", required = false) boolean spec) {
        try {
            AuthenticationProfileModel authenticationProfileModel = (AuthenticationProfileModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            MockEntities mock = this.mockModel.getById(id);
            MockVmodel mockResponseVmodel = new MockVmodel();
            mockResponseVmodel.setId(mock.getId());
            mockResponseVmodel.setSpec(Json.mapper().readTree(mock.getSwagger()));
            mockResponseVmodel.setDescription(mock.getDescription());
            mockResponseVmodel.setTitle(mock.getTitle());
            //check if the user is exist
            if (mock.getUsers() != null) {
                List<UserMocks> userMockss = new ArrayList<>();
                for (UserMocksEntities userMocksEntities : mock.getUsers()) {
                    if (userMocksEntities.getUserId().equals(authenticationProfileModel.get_id())) {
                        UserMocks userMocks = new UserMocks();
                        userMocks.setUserId(userMocksEntities.getUserId());
                        userMocks.setAccess(userMocksEntities.getAccess());
                        userMockss.add(userMocks);
                        break;
                    }
                }
                mockResponseVmodel.setUsers(userMockss);
            }
            if (spec)
                return ResponseEntity.ok(mockResponseVmodel.getSpec());
            return ResponseEntity.ok(mockResponseVmodel);
        } catch (NotFoundException e) {
            return this.handlingErrorResponse(e.getErrorModel(), e);
        } catch (Exception e) {
            return this.handlingErrorResponse(null, e);
        }
    }

    //    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getUserMocks(@PathVariable String id) {
        return ResponseEntity.ok(this.mockModel.getUsersListOfMocks(id));
    }

    @GetMapping(value = "/mocks/{id}/histories",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> getMockHistories(@PathVariable String id) {
        return ResponseEntity.ok(this.mockModel.getMockHistories(id));
    }

    /**
     * @param body
     * @return
     * @throws JsonProcessingException
     * @desc storing the mocks
     */
    @PreAuthorize("hasAnyAuthority('MOCKS_READ_WRITE')")
    @PostMapping(value = "/mocks/store")
    public ResponseEntity storeMocksEntity(@RequestBody MockVmodel body) {
        log.info("{}", body);
        try {
            AuthenticationProfileModel authenticationProfileModel = (AuthenticationProfileModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            MockEntities mock = this.mockModel.save(body, authenticationProfileModel);
            Paths newPath = new Paths();
            body.setId(mock.getId());
            OpenAPI openAPI = Json.mapper().readValue(mock.getSpec(), OpenAPI.class);
            openAPI.getPaths().forEach((s, pathItem) -> {
                newPath.put(s.replace("_", ".").replace("*{", "{"), pathItem);
            });
            openAPI.setPaths(newPath);
            body.setSpec(Json.mapper().readTree(mock.getSwagger()));

            return ResponseEntity.ok(body);
        } catch (InvalidMockException e) {
            return this.handlingErrorResponse(e.getErrorModel(), e);
        } catch (Exception e) {
            return this.handlingErrorResponse(null, e);
        }
    }

    @GetMapping(value = "/health",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity healthCheck() {
        try {
            BsonDocument b = new BsonDocument();
            b.put("ping", new BsonString("1"));
            Document doc = mongoTemplate.getDb().runCommand(b);
            if (doc.get("ok").equals(1.0)) {
                return ResponseEntity.ok("up");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("down");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * @param id
     * @return
     * @desc delete mock by id
     */
    @PreAuthorize("hasAnyAuthority('MOCKS_READ_WRITE')")
    @DeleteMapping(value = "/mocks/{id}/delete",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity deleteByMockId(@PathVariable String id) {
        try {
            AuthenticationProfileModel authenticationProfileModel = (AuthenticationProfileModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            this.mockModel.deleteById(id, authenticationProfileModel);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return this.handlingErrorResponse(e.getErrorModel(), e);
        } catch (UnathorizedAccess e) {
            return this.handlingErrorResponse(e.getErrorModel(), e);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/{id}/spec", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getMockSpecById(@PathVariable String id) {
        try {
            MockEntities mock = this.mockModel.getById(id);
            MockVmodel mockResponseVmodel = new MockVmodel();
            mockResponseVmodel.setSpec(Json.mapper().readTree(mock.getSwagger()));
            return ResponseEntity.ok(mockResponseVmodel.getSpec());
        } catch (NotFoundException e) {
            return this.handlingErrorResponse(e.getErrorModel(), e);
        } catch (Exception e) {
            return this.handlingErrorResponse(null, e);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ_WRITE')")
    @PutMapping(value = "/mocks/{id}/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateMockById(@PathVariable String id, @RequestBody MockVmodel body) {
        try {
            AuthenticationProfileModel authenticationProfileModel = (AuthenticationProfileModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            MockEntities mock = this.mockModel.updateByID(id, body, authenticationProfileModel);
            Paths newPath = new Paths();
            body.setId(mock.getId());
            OpenAPI openAPI = Json.mapper().readValue(mock.getSpec(), OpenAPI.class);
            openAPI.getPaths().forEach((s, pathItem) -> {
                newPath.put(s.replace("_", ".").replace("*{", "{"), pathItem);
            });
            openAPI.setPaths(newPath);
            body.setSpec(Json.mapper().readTree(mock.getSwagger()));
            return ResponseEntity.ok(body);
        } catch (NotFoundException e) {
            return this.handlingErrorResponse(e.getErrorModel(), e);
        } catch (UnathorizedAccess e) {
            return this.handlingErrorResponse(e.getErrorModel(), e);
        } catch (InvalidMockException e) {
            return this.handlingErrorResponse(e.getErrorModel(), e);
        } catch (JsonProcessingException e) {
            return this.handlingErrorResponse(ResponseCode.INVALID_MOCKUP_STRUCTURE, e);
        }
    }

    @PreAuthorize("hasAnyAuthority('MOCKS_READ','MOCKS_READ_WRITE')")
    @GetMapping(value = "/mocks/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getMocksPagination(
            Pageable pageable,
            @RequestParam(value = "q", required = false) String q) {
        try {
            AuthenticationProfileModel authenticationProfileModel = (AuthenticationProfileModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            MockEntitiesPage pagingVmodel = this.mockModel.paging(pageable, q, authenticationProfileModel);
            return ResponseEntity.ok(pagingVmodel);
        } catch (Exception e) {
            return this.handlingErrorResponse(null, e);
        }
    }
}
